/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.aspects.Immutable;
import com.jcabi.log.Logger;
import com.jcabi.w3c.Validator;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher for checking HTML and CSS documents against W3C validation services.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @since 0.1
 */
@Immutable
@ToString
@EqualsAndHashCode(callSuper = false, of = "validator")
final class W3CValidatorMatcher extends TypeSafeMatcher<String> {

    /**
     * The W3C Validator.
     */
    private final transient Validator validator;

    /**
     * Ctor.
     * @param val The Validator to use.
     */
    W3CValidatorMatcher(final Validator val) {
        super();
        this.validator = val;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("W3C validator")
            .appendText(this.validator.toString());
    }

    @Override
    public boolean matchesSafely(final String content) {
        boolean matches = false;
        try {
            matches = this.validator.validate(content).valid();
        } catch (final IOException ex) {
            Logger.warn(
                this,
                "#matchesSafely('%s'): unable to perform validation: %s",
                content, ex.getMessage()
            );
        }
        return matches;
    }

}
