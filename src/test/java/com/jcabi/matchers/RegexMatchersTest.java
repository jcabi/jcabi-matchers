/**
 * Copyright (c) 2011-2017, jcabi.com
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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link RegexMatchers}.
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 1.3
 */
public final class RegexMatchersTest {

    /**
     * RegexMatchers should be able to match a string against a series
     * of patterns.
     */
    @Test
    public void matchesStringToPatterns() {
        MatcherAssert.assertThat(
            "zxc456",
            RegexMatchers.matchesAnyPattern("[zxc]+\\d{3}", "[abc]+")
        );
    }

    /**
     * RegexMatchers should be able to match a string against a given pattern.
     */
    @Test
    public void matchesStringToPattern() {
        MatcherAssert.assertThat(
            "abc123",
            Matchers.allOf(
                RegexMatchers.matchesPattern("[a-c]+\\d{3}"),
                Matchers.not(RegexMatchers.matchesPattern("[d-f]+\\d{4}"))
            )
        );
    }

    /**
     * RegexMatchers should be able to check if a String contains a given
     * pattern.
     */
    @Test
    public void checksIfStringContainsPattern() {
        MatcherAssert.assertThat(
            "aardvark",
            Matchers.allOf(
                RegexMatchers.containsPattern("rdva"),
                Matchers.not(RegexMatchers.matchesPattern("foo"))
            )
        );
    }

    /**
     * RegexMatchers should be able to check if a String contains any of the
     * given patterns.
     */
    @Test
    public void checksIfStringContainsAnyPattern() {
        MatcherAssert.assertThat(
            "awrjbvjkb",
            Matchers.allOf(
                RegexMatchers.containsAnyPattern("aa", "bb", "jbv"),
                Matchers.not(RegexMatchers.containsAnyPattern("cc", "dd"))
            )
        );
    }

    /**
     * RegexMatchers should be able to check if a String contains all of the
     * given patterns.
     */
    @Test
    public void checksIfStringContainsAllPatterns() {
        MatcherAssert.assertThat(
            "asjbclkjbxhui",
            Matchers.allOf(
                RegexMatchers.containsAllPatterns("asj", "lkj", "jbx"),
                Matchers.not(RegexMatchers.containsAllPatterns("bcl", "ff"))
            )
        );
    }

}
