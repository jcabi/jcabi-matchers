/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.w3c.Defect;
import com.jcabi.w3c.ValidationResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link W3CValidatorMatcher}.
 *
 * @since 2.0
 */
final class W3CValidatorMatcherTest {

    @Test
    void acceptsValidContent() {
        MatcherAssert.assertThat(
            "should pass the content to the validator and accept its result",
            "valid content",
            new W3CValidatorMatcher(
                content -> new W3CValidatorMatcherTest.Result("valid content".equals(content))
            )
        );
    }

    @Test
    void rejectsInvalidContent() {
        MatcherAssert.assertThat(
            "should reject the validator's negative result",
            "invalid content",
            Matchers.not(
                new W3CValidatorMatcher(
                    content -> new W3CValidatorMatcherTest.Result(false)
                )
            )
        );
    }

    @Test
    void rejectsUnavailableValidator() {
        MatcherAssert.assertThat(
            "should reject content when validation fails",
            "unavailable service",
            Matchers.not(
                new W3CValidatorMatcher(
                    content -> {
                        throw new IOException("Validator is unavailable");
                    }
                )
            )
        );
    }

    /**
     * A validation result that does not contact a public service.
     *
     * @since 2.0
     */
    private static final class Result implements ValidationResponse {

        /**
         * Whether validation succeeded.
         */
        private final boolean success;

        /**
         * New result.
         *
         * @param valid Whether validation succeeded
         */
        Result(final boolean valid) {
            this.success = valid;
        }

        @Override
        public boolean valid() {
            return this.success;
        }

        @Override
        public URI checkedBy() {
            return URI.create("https://validator.example.com/");
        }

        @Override
        public String doctype() {
            return "html";
        }

        @Override
        public Charset charset() {
            return StandardCharsets.UTF_8;
        }

        @Override
        public Set<Defect> errors() {
            return Collections.emptySet();
        }

        @Override
        public Set<Defect> warnings() {
            return Collections.emptySet();
        }
    }
}
