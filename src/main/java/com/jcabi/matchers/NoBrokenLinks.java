/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

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
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Finds broken links in HTML.
 *
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
    private final transient Collection<URI> broken;

    /**
     * Public ctor.
     * @param uri Home page URI, for relative links
     */
    public NoBrokenLinks(final URI uri) {
        super();
        this.home = uri;
        this.broken = new LinkedList<>();
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
                this.broken.size(), this.broken
            )
        );
    }

    /**
     * Check for validness.
     * @param response Response to check
     */
    private void check(final Response response) {
        final Collection<String> links = new XmlResponse(response).xml().xpath(
            new StringBuilder("//head/link/@href")
                .append(" | //body//a/@href")
                .append(" | //body//img/@src")
                .append(" | //xhtml:img/@src")
                .append(" | //xhtml:a/@href")
                .append(" | //xhtml:link/@href")
                .toString()
        );
        Logger.debug(
            this, "#assertThat(): %d links found: %[list]s",
            links.size(), links
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
                    "#isValid('%s'): not valid since response code is %d",
                    uri, code
                );
            }
        } catch (final MalformedURLException ex) {
            Logger.warn(
                NoBrokenLinks.class,
                "#isValid('%s'): invalid URL: %s",
                uri, ex.getMessage()
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
                Logger.debug(
                    NoBrokenLinks.class,
                    "#http('%s'): response code is %s",
                    url, code
                );
            } catch (final IOException ex) {
                Logger.warn(
                    NoBrokenLinks.class,
                    "#http('%s'): can't get response code: %s",
                    url, ex.getMessage()
                );
            } finally {
                conn.disconnect();
            }
        } catch (final IOException ex) {
            Logger.warn(
                NoBrokenLinks.class,
                "#http('%s'): can't open connection: %s",
                url, ex.getMessage()
            );
        }
        return code;
    }

}
