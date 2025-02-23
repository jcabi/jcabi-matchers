/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
