/**
 * Copyright (c) 2011-2013, ReXSL.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the ReXSL.com nor
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
package com.rexsl.test;

import com.jcabi.aspects.Loggable;
import com.jcabi.xml.XPathContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.w3c.dom.Node;

/**
 * Convenient set of matchers for XHTML/XML content.
 *
 * <p>For example:
 *
 * <pre> MatcherAssert.assertThat(
 *   "&lt;root&gt;&lt;a&gt;hello&lt;/a&gt;&lt;/root&gt;",
 *   XhtmlMatchers.hasXPath("/root/a[.='hello']")
 * );</pre>
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.2.6
 */
@ToString
@EqualsAndHashCode
@Loggable(Loggable.DEBUG)
public final class XhtmlMatchers {

    /**
     * Private ctor, it's a utility class.
     */
    private XhtmlMatchers() {
        // intentionally empty
    }

    /**
     * Makes XHTML source presentable for testing.
     *
     * <p>Useful method for assertions in unit tests. For example:
     *
     * <pre> MatcherAssert.assertThat(
     *   XhtmlMatchers.xhtml(dom_xml_element),
     *   XhtmlMatchers.hasXPath("/root/data")
     * );</pre>
     *
     * <p>The method understands different input types differently. For example,
     * an {@link InputStream} will be read as a UTF-8 document, {@link Reader}
     * will be read as a document, a {@link Source} will be used "as is",
     * {@link Node} will be printed as a text, etc. The goal is to make any
     * input type presentable as an XML document, as much as it is possible.
     *
     * @param xhtml The source of data
     * @return Renderable source
     * @param <T> Type of source
     * @since 0.4.10
     */
    @NotNull
    public static <T> Source xhtml(@NotNull final T xhtml) {
        Source source;
        if (xhtml instanceof Source) {
            source = Source.class.cast(xhtml);
        } else if (xhtml instanceof InputStream) {
            final InputStream stream = InputStream.class.cast(xhtml);
            try {
                source = new StringSource(IOUtils.toString(stream));
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } else if (xhtml instanceof Reader) {
            final Reader reader = Reader.class.cast(xhtml);
            try {
                source = new StringSource(IOUtils.toString(reader));
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            } finally {
                IOUtils.closeQuietly(reader);
            }
        } else if (xhtml instanceof Node) {
            source = new StringSource(Node.class.cast(xhtml));
        } else {
            source = new StringSource(xhtml.toString());
        }
        return source;
    }

    /**
     * Matches content against XPath query.
     * @param query The query
     * @return Matcher suitable for JUnit/Hamcrest matching
     * @param <T> Type of XML content provided
     */
    @NotNull
    public static <T> Matcher<T> hasXPath(@NotNull final String query) {
        return XhtmlMatchers.hasXPath(query, new XPathContext());
    }

    /**
     * Matches content against XPath query, with custom namespaces.
     *
     * <p>Every namespace from the {@code namespaces} list will be assigned to
     * its own prefix, in order of appearance. Start with {@code 1}.
     * For example:
     *
     * <pre> MatcherAssert.assert(
     *   "&lt;foo xmlns='my-namespace'&gt;&lt;/foo&gt;",
     *   XhtmlMatchers.hasXPath("/ns1:foo", "my-namespace")
     * );</pre>
     *
     * @param query The query
     * @param namespaces List of namespaces
     * @return Matcher suitable for JUnit/Hamcrest matching
     * @param <T> Type of XML content provided
     */
    @NotNull
    public static <T> Matcher<T> hasXPath(@NotNull final String query,
        final Object... namespaces) {
        return XhtmlMatchers.hasXPath(query, new XPathContext(namespaces));
    }

    /**
     * Matches content against XPath query, with custom context.
     * @param query The query
     * @param ctx The context
     * @return Matcher suitable for JUnit/Hamcrest matching
     * @param <T> Type of XML content provided
     */
    @NotNull
    public static <T> Matcher<T> hasXPath(@NotNull final String query,
        @NotNull final NamespaceContext ctx) {
        return new XPathMatcher<T>(query, ctx);
    }

    /**
     * Matches content against list of XPaths.
     * @param xpaths The query
     * @return Matcher suitable for JUnit/Hamcrest matching
     * @param <T> Type of XML content provided
     */
    @NotNull
    public static <T> Matcher<T> hasXPaths(final String...xpaths) {
        final Collection<Matcher<? super T>> list =
            new ArrayList<Matcher<? super T>>(xpaths.length);
        for (final String xpath : xpaths) {
            list.add(XhtmlMatchers.<T>hasXPath(xpath));
        }
        return Matchers.allOf(list);
    }
}
