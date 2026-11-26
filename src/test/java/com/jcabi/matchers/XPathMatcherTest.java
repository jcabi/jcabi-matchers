/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

/**
 * Namespace handling tests for {@link XPathMatcher}.
 *
 * @since 0.3.7
 */
final class XPathMatcherTest {

    @Test
    void preservesCdataThatLooksLikeANamespace() {
        MatcherAssert.assertThat(
            "<root><![CDATA[ xmlns=\"demo\"]]></root>",
            XhtmlMatchers.hasXPath("/root[.=' xmlns=\"demo\"']")
        );
    }

    @Test
    void preservesAttributeContentWhenIgnoringDefaultNamespace() {
        MatcherAssert.assertThat(
            "<root xmlns='urn:root'><child note=' xmlns=\"nested\"'/></root>",
            XhtmlMatchers.hasXPath("/root/child[@note=' xmlns=\"nested\"']")
        );
    }
}
