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

import com.jcabi.aspects.Tv;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;

/**
 * Matcher that test if all matchers matches, but print info about only that
 * ones who failed.
 *
 * Based in {@link AllOf}
 *
 * @author Jose V. Dal Pra Junior (jrdalpra@gmail.com)
 * @version $Id$
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
        final Iterable<Matcher<? super T>> iterable) {
        super();
        this.matchers = iterable;
        this.wrong = new ArrayList<Matcher<? super T>>(Tv.THREE);
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
