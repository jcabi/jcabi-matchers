/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;

/**
 * Matcher that test if all matchers matches, but print info about only that
 * ones who failed.
 *
 * Based in {@link AllOf}.
 *
 * @param <T> Type of argument
 * @since 0.2.6
 */
final class AllOfThatPrintsOnlyWrongMatchers<T> extends DiagnosingMatcher<T> {

    /**
     * Matchers that will be tested.
     */
    private final transient Iterable<Matcher<? super T>> matchers;

    /**
     * Matchers that does not matches.
     */
    private final transient List<Matcher<? super T>> wrong;

    /**
     * Construct that accept matchers to test.
     * @param iterable Matchers that will be tested.
     */
    AllOfThatPrintsOnlyWrongMatchers(
        final Iterable<Matcher<? super T>> iterable
    ) {
        super();
        this.matchers = iterable;
        this.wrong = new ArrayList<>(3);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendList("(", ",", ")", this.wrong);
    }

    @Override
    public boolean matches(final Object obj, final Description mismatch) {
        boolean matches = true;
        for (final Matcher<? super T> matcher : this.matchers) {
            if (!matcher.matches(obj)) {
                mismatch.appendDescriptionOf(matcher).appendText(" ");
                matcher.describeMismatch(obj, mismatch);
                this.wrong.add(matcher);
                matches = false;
            }
        }
        return matches;
    }

}
