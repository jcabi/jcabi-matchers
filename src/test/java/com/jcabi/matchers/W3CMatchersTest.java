/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.w3c.ValidatorBuilder;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link W3CMatchers}.
 *
 * @since 0.1
 */
final class W3CMatchersTest {

    @Test
    void createsHtmlMatcher() {
        MatcherAssert.assertThat(
            "HTML factory should use the HTML validator",
            StringDescription.asString(W3CMatchers.validHtml()),
            Matchers.equalTo(
                StringDescription.asString(
                    new W3CValidatorMatcher(ValidatorBuilder.HTML)
                )
            )
        );
    }

    @Test
    void createsCssMatcher() {
        MatcherAssert.assertThat(
            "CSS factory should use the CSS validator",
            StringDescription.asString(W3CMatchers.validCss()),
            Matchers.equalTo(
                StringDescription.asString(
                    new W3CValidatorMatcher(ValidatorBuilder.CSS)
                )
            )
        );
    }
}
