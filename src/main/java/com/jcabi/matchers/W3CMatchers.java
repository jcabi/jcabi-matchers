/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.w3c.ValidatorBuilder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Matcher;

/**
 * Matchers for validating HTML and CSS content.
 *
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class W3CMatchers {

    /**
     * Default HTML online validator.
     */
    private static final W3CValidatorMatcher HTML =
        new W3CValidatorMatcher(ValidatorBuilder.HTML);

    /**
     * Default online CSS validator.
     */
    private static final W3CValidatorMatcher CSS =
        new W3CValidatorMatcher(ValidatorBuilder.CSS);

    /**
     * Private ctor, it's a utility class.
     */
    private W3CMatchers() {
        // intentionally empty
    }

    /**
     * Matcher for validating HTML content against W3C validation servers.
     * @return Matcher for validating HTML content.
     */
    public static Matcher<String> validHtml() {
        return W3CMatchers.HTML;
    }

    /**
     * Matcher for validating CSS content against W3C validation servers.
     * @return Matcher for validating CSS content.
     */
    public static Matcher<String> validCss() {
        return W3CMatchers.CSS;
    }

}
