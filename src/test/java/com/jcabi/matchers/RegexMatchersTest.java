/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link RegexMatchers}.
 * @since 1.3
 */
final class RegexMatchersTest {

    @Test
    void matchesStringToPatterns() {
        MatcherAssert.assertThat(
            "zxc456",
            RegexMatchers.matchesAnyPattern("[zxc]+\\d{3}", "[abc]+")
        );
    }

    @Test
    void matchesStringToPattern() {
        MatcherAssert.assertThat(
            "abc123",
            Matchers.allOf(
                RegexMatchers.matchesPattern("[a-c]+\\d{3}"),
                Matchers.not(RegexMatchers.matchesPattern("[d-f]+\\d{4}"))
            )
        );
    }

    @Test
    void checksIfStringContainsPattern() {
        MatcherAssert.assertThat(
            "aardvark",
            Matchers.allOf(
                RegexMatchers.containsPattern("rdva"),
                Matchers.not(RegexMatchers.matchesPattern("foo"))
            )
        );
    }

    @Test
    void checksIfStringContainsAnyPattern() {
        MatcherAssert.assertThat(
            "awrjbvjkb",
            Matchers.allOf(
                RegexMatchers.containsAnyPattern("aa", "bb", "jbv"),
                Matchers.not(RegexMatchers.containsAnyPattern("cc", "dd"))
            )
        );
    }

    @Test
    void checksIfStringContainsAllPatterns() {
        MatcherAssert.assertThat(
            "asjbclkjbxhui",
            Matchers.allOf(
                RegexMatchers.containsAllPatterns("asj", "lkj", "jbx"),
                Matchers.not(RegexMatchers.containsAllPatterns("bcl", "ff"))
            )
        );
    }

}
