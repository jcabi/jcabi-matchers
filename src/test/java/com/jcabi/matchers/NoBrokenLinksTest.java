/*
 * Copyright (c) 2011-2023, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
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
package com.jcabi.matchers;

import com.jcabi.http.request.FakeRequest;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link NoBrokenLinks}.
 * @since 0.1
 */
final class NoBrokenLinksTest {

    @Test
    void findsEmptyLinksInHtml() throws Exception {
        MatcherAssert.assertThat(
            new FakeRequest().withBody(
                StringUtils.join(
                    "<html xmlns='http://www.w3.org/1999/xhtml'><head>",
                    "<link rel='stylesheet' href=''/></head></html>"
                )
            ).fetch(),
            Matchers.not(
                new NoBrokenLinks(new URI("http://www.facebook.com/"))
            )
        );
    }

    @Test
    void findsBrLinksInHtmlWithNamespace() throws Exception {
        MatcherAssert.assertThat(
            new FakeRequest().withBody(
                StringUtils.join(
                    "<html xmlns='http://www.w3.org/1999/xhtml' >",
                    "<head><link rel='stylesheet' href='/broken-link'/>",
                    "</head> </html>"
                )
            ).fetch(),
            Matchers.not(
                new NoBrokenLinks(new URI("http://google.com"))
            )
        );
    }

    @Test
    void passesWithoutBrokenLinks() throws Exception {
        MatcherAssert.assertThat(
            new FakeRequest().withBody(
                StringUtils.join(
                    "<html xmlns='http://www.w3.org/1999/xhtml'>",
                    "<body><a href='/index.html'/>",
                    "<a href='http://img.teamed.io/logo.svg'/>",
                    "</body></html>"
                )
            ).fetch(),
            new NoBrokenLinks(new URI("http://www.teamed.io/"))
        );
    }

    @Test
    void throwsWhenHtmlIsBroken() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () ->
                MatcherAssert.assertThat(
                    new FakeRequest().withBody("not HTML at all").fetch(),
                    new NoBrokenLinks(new URI("#"))
                )
        );
    }

}
