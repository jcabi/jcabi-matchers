/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.aspects.Immutable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher of Regex patterns against a String, similar to
 * {@link String#matches(String)}.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @since 1.3
 */
@Immutable
@ToString
@EqualsAndHashCode(callSuper = false, of = "pattern")
final class RegexMatchingPatternMatcher extends TypeSafeMatcher<String> {

    /**
     * The Regex pattern.
     */
    private final transient String pattern;

    /**
     * Public ctor.
     * @param regex The regular expression to match against.
     */
    RegexMatchingPatternMatcher(final String regex) {
        super();
        this.pattern = regex;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("a String matching the regular expression ")
            .appendText(this.pattern);
    }

    @Override
    public boolean matchesSafely(final String item) {
        return item.matches(this.pattern);
    }

}
