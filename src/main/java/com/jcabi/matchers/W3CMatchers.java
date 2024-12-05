/*
 * Copyright (c) 2011-2024, jcabi.com
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
