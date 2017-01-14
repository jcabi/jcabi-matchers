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

import java.util.ArrayList;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

/**
 * Convenient matchers for checking Strings against regular expressions.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 * @since 1.3
 */
@ToString
@EqualsAndHashCode
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class RegexMatchers {

    /**
     * Private ctor, it's a utility class.
     */
    private RegexMatchers() {
        // Utility class, shouldn't be instantiated.
    }

    /**
     * Checks whether a String matches at lease one of given regular
     * expressions.
     * @param patterns Regular expression patterns
     * @return Matcher suitable for JUnit/Hamcrest matching
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public static Matcher<String> matchesAnyPattern(final String...patterns) {
        final Collection<Matcher<? super String>> matchers =
            new ArrayList<>(patterns.length);
        for (final String pattern : patterns) {
            matchers.add(new RegexMatchingPatternMatcher(pattern));
        }
        return CoreMatchers.anyOf(matchers);
    }

    /**
     * Checks whether a String matches the given regular expression. Works in a
     * similar manner to {@link String#matches(String)}. For example:
     *
     * <pre> MatcherAssert.assert(
     *   "abc123",
     *   RegexMatchers.matchesPattern("[a-c]+\\d{3}")
     * );</pre>
     *
     * @param pattern The pattern to match against
     * @return Matcher suitable for JUnit/Hamcrest matching
     */
    public static Matcher<String> matchesPattern(final String pattern) {
        return new RegexMatchingPatternMatcher(pattern);
    }

    /**
     * Checks whether a String contains a subsequence matching the given regular
     * expression. Works in a similar manner to
     * {@link java.util.regex.Matcher#find()}. For example:
     *
     * <pre> MatcherAssert.assert(
     *   "fooBar123",
     *   RegexMatchers.containsPattern("Bar12")
     * );</pre>
     *
     * @param pattern The pattern to match against
     * @return Matcher suitable for JUnit/Hamcrest matching
     */
    public static Matcher<String> containsPattern(final String pattern) {
        return new RegexContainingPatternMatcher(pattern);
    }

    /**
     * Checks whether a {@link String} contains a subsequence matching any of
     * the given regular expressions.
     * @param patterns The patterns to match against
     * @return Matcher suitable for JUnit/Hamcrest matching
     * @see java.util.regex.Matcher#find()
     * @see #containsPattern(String)
     */
    public static Matcher<String> containsAnyPattern(final String... patterns) {
        return CoreMatchers
            .anyOf(createContainingMatchers(patterns));
    }

    /**
     * Checks whether a {@link String} contains a subsequence matching any of
     * the given regular expressions.
     * @param patterns The patterns to match against
     * @return Matcher suitable for JUnit/Hamcrest matching
     * @see java.util.regex.Matcher#find()
     * @see #containsPattern(String)
     */
    public static Matcher<String> containsAllPatterns(
        final String... patterns) {
        return CoreMatchers
            .allOf(createContainingMatchers(patterns));
    }

    /**
     * Creates a {@link Collection} of {@link Matcher}'s for the given patterns.
     * @param patterns The given patterns
     * @return A {@link Collection} of {@link Matcher}'s
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private static Collection<Matcher<? super String>> createContainingMatchers(
        final String... patterns) {
        final Collection<Matcher<? super String>> matchers =
            new ArrayList<>(patterns.length);
        for (final String pattern : patterns) {
            matchers.add(new RegexContainingPatternMatcher(pattern));
        }
        return matchers;
    }

}
