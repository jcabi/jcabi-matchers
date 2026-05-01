/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link StringSource}.
 * @since 0.1
 */
final class StringSourceTest {

    @Test
    void formatsIncomingXmlDocument() {
        MatcherAssert.assertThat(
            "should contains a string",
            new StringSource("<a><b>\u0443\u0440\u0430!</b></a>").toString(),
            Matchers.containsString("&#443;")
        );
    }

    @Test
    void formatIncomingNode() throws Exception {
        MatcherAssert.assertThat(
            "should equals to the node",
            new StringSource(
                DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder().parse(
                        new ByteArrayInputStream(
                            StringUtils.join(
                                "<nodeName><?some instruction?><!--comment-->",
                                "<a>withText</a><a/><a withArg='value'/></nodeName>"
                            ).getBytes(StandardCharsets.UTF_8)
                        )
                    )
            ).toString(),
            // @checkstyle ProhibitLineSeparatorInStringsCheck (7 lines)
            Matchers.equalTo(
                StringUtils.join(
                    "<nodeName><?some instruction?>",
                    "<!--comment-->\n   ",
                    "<a>withText</a>\n   <a/>\n   <a withArg=\"value\"/>\n",
                    "</nodeName>\n"
                )
            )
        );
    }
}
