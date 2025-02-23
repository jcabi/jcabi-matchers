/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.aspects.RetryOnFailure;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link W3CMatchers}.
 * @since 0.1
 */
final class W3CMatchersTest {

    @Test
    @RetryOnFailure(verbose = false)
    void matchesValidHtml() {
        MatcherAssert.assertThat(
            StringUtils.join(
                "<!DOCTYPE html>",
                "<html lang='en'><head><meta charset='UTF-8'>",
                "<title>hey</title></head>",
                "<body></body></html>"
            ),
            W3CMatchers.validHtml()
        );
    }

    @Test
    @RetryOnFailure(verbose = false)
    void rejectsInvalidHtml() {
        MatcherAssert.assertThat(
            "<blah><blaaaaaaaaa/>",
            Matchers.not(W3CMatchers.validHtml())
        );
    }

    @Test
    @RetryOnFailure(verbose = false)
    void matchesValidCss() {
        MatcherAssert.assertThat(
            "body { background-color:#d0e4fe; }",
            W3CMatchers.validCss()
        );
    }

    @Test
    @Disabled("I have no idea why it doesn't work")
    @RetryOnFailure(verbose = false)
    void rejectsInvalidCss() {
        MatcherAssert.assertThat(
            "div { -- $#^@*&^$&@; }",
            Matchers.not(W3CMatchers.validCss())
        );
    }
}
