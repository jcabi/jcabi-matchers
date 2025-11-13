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
import org.junit.jupiter.api.Test;

/**
 * Integration case for {@link NoBrokenLinks}.
 * @since 0.1
 */
final class NoBrokenLinksITCase {

    @Test
    void findsBrokenLinksInHtml() throws Exception {
        MatcherAssert.assertThat(
            "should finds the broken links",
            new FakeRequest().withBody(
                StringUtils.join(
                    "<html xmlns='http://www.w3.org/1999/xhtml'><head>",
                    "<link rel='stylesheet' href='http://localhost:7878'/>",
                    "</head></html>"
                )
            ).fetch(),
            Matchers.not(
                new NoBrokenLinks(new URI("http://www.google.com/"))
            )
        );
    }

}
