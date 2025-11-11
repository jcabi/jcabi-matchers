/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

/**
 * Test case for {@link StringSource}.
 * @since 0.1
 */
final class StringSourceTest {

    @Test
    void formatsIncomingXmlDocument() {
        final String xml = "<a><b>\u0443\u0440\u0430!</b></a>";
        MatcherAssert.assertThat(
            "should contains a string",
            new StringSource(xml).toString(),
            Matchers.containsString("&#443;")
        );
    }

    @Test
    void formatIncomingNode() throws Exception {
        final DocumentBuilder builder = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder();
        final String xml = StringUtils.join(
            "<nodeName><?some instruction?><!--comment-->",
            "<a>withText</a><a/><a withArg='value'/></nodeName>"
        );
        final Node node = builder.parse(
            new ByteArrayInputStream(xml.getBytes())
        );
        MatcherAssert.assertThat(
            "should equals to the node",
            new StringSource(node).toString(),
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
