/**
 * Copyright (c) 2011-2013, ReXSL.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the ReXSL.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rexsl.test.html;

import com.jcabi.http.Response;
import com.jcabi.http.response.XmlResponse;
import com.jcabi.log.Logger;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matches HTTP header against required value.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.3.4
 */
@ToString
@EqualsAndHashCode(callSuper = false, of = "home")
public final class NoBrokenLinks extends BaseMatcher<Response> {

    /**
     * Home page.
     */
    private final transient URI home;

    /**
     * List of broken links.
     */
    private final transient Collection<URI> broken = new LinkedList<URI>();

    /**
     * Public ctor.
     * @param uri Home page URI, for relative links
     */
    public NoBrokenLinks(final URI uri) {
        super();
        this.home = uri;
    }

    @Override
    public boolean matches(final Object item) {
        this.check(Response.class.cast(item));
        return this.broken.isEmpty();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(
            Logger.format(
                "%d broken link(s) found: %[list]s",
                this.broken.size(),
                this.broken
            )
        );
    }

    /**
     * Check for validness.
     * @param response Response to check
     */
    private void check(final Response response) {
        final Collection<String> links = new XmlResponse(response).xml().xpath(
            StringUtils.join(
                "//head/link/@href",
                " | //body//a/@href",
                " | //body//img/@src",
                " | //xhtml:img/@src",
                " | //xhtml:a/@href",
                " | //xhtml:link/@href"
            )
        );
        Logger.debug(
            this,
            "#assertThat(): %d links found: %[list]s",
            links.size(),
            links
        );
        this.broken.clear();
        for (final String link : links) {
            final URI uri;
            if (link.isEmpty() || link.charAt(0) != '/') {
                uri = URI.create(link);
            } else {
                uri = this.home.resolve(link);
            }
            if (!uri.isAbsolute() || !NoBrokenLinks.isValid(uri)) {
                this.broken.add(uri);
            }
        }
    }

    /**
     * Check whether the URI is valid and returns code 200.
     * @param uri The URI to check
     * @return TRUE if it's valid
     */
    private static boolean isValid(final URI uri) {
        boolean valid = false;
        try {
            final int code = NoBrokenLinks.http(uri.toURL());
            if (code < HttpURLConnection.HTTP_BAD_REQUEST) {
                valid = true;
            } else {
                Logger.warn(
                    NoBrokenLinks.class,
                    "#isValid('%s'): not valid since responde code=%d",
                    uri,
                    code
                );
            }
        } catch (MalformedURLException ex) {
            Logger.warn(
                NoBrokenLinks.class,
                "#isValid('%s'): invalid URL: %s",
                uri,
                ex.getMessage()
            );
        }
        return valid;
    }

    /**
     * Get HTTP response code from this URL.
     * @param url The URL to get
     * @return HTTP response code
     */
    private static int http(final URL url) {
        int code = HttpURLConnection.HTTP_BAD_REQUEST;
        try {
            final HttpURLConnection conn =
                HttpURLConnection.class.cast(url.openConnection());
            try {
                code = conn.getResponseCode();
            } catch (IOException ex) {
                Logger.warn(
                    NoBrokenLinks.class,
                    "#http('%s'): can't get response code: %s",
                    url,
                    ex.getMessage()
                );
            } finally {
                conn.disconnect();
            }
        } catch (IOException ex) {
            Logger.warn(
                NoBrokenLinks.class,
                "#http('%s'): can't open connection: %s",
                url,
                ex.getMessage()
            );
        }
        return code;
    }

}
