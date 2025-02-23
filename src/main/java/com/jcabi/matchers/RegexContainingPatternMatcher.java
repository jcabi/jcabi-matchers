/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Checks if a given string contains a subsequence matching the given pattern,
 * similar to {@link java.util.regex.Matcher#find()}.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @since 1.3
 */
@ToString
@EqualsAndHashCode(callSuper = false, of = "pattern")
final class RegexContainingPatternMatcher extends TypeSafeMatcher<String> {

    /**
     * The Regex pattern.
     */
    private final transient Pattern pattern;

    /**
     * Public ctor.
     * @param regex The regular expression to match against.
     */
    RegexContainingPatternMatcher(final String regex) {
        super();
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("a String containing the regular expression ")
            .appendText(this.pattern.toString());
    }

    @Override
    public boolean matchesSafely(final String item) {
        return this.pattern.matcher(item).find();
    }

}
