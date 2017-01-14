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

import com.jcabi.xml.XPathContext;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Matcher;
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
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
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
     * @param <T> Type of source
     * @return Renderable source
     * @since 0.4.10
     */
    public static <T> Source xhtml(final T xhtml) {
        final Source source;
        if (xhtml instanceof Source) {
            source = Source.class.cast(xhtml);
        } else if (xhtml instanceof InputStream) {
            final InputStream stream = InputStream.class.cast(xhtml);
            try {
                source = new StringSource(
                    readAsString(new InputStreamReader(stream, "UTF-8"))
                );
            } catch (final UnsupportedEncodingException ex) {
                throw new IllegalStateException(ex);
            }
        } else if (xhtml instanceof Reader) {
            final Reader reader = Reader.class.cast(xhtml);
            source = new StringSource(readAsString(reader));
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
     * @param <T> Type of XML content provided
     * @return Matcher suitable for JUnit/Hamcrest matching
     */
    public static <T> Matcher<T> hasXPath(final String query) {
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
     * @param <T> Type of XML content provided
     * @return Matcher suitable for JUnit/Hamcrest matching
     */
    public static <T> Matcher<T> hasXPath(final String query,
        final Object... namespaces) {
        return XhtmlMatchers.hasXPath(query, new XPathContext(namespaces));
    }

    /**
     * Matches content against XPath query, with custom context.
     * @param query The query
     * @param ctx The context
     * @param <T> Type of XML content provided
     * @return Matcher suitable for JUnit/Hamcrest matching
     */
    public static <T> Matcher<T> hasXPath(final String query,
        final NamespaceContext ctx) {
        return new XPathMatcher<T>(query, ctx);
    }

    /**
     * Matches content against list of XPaths.
     * @param xpaths The query
     * @param <T> Type of XML content provided
     * @return Matcher suitable for JUnit/Hamcrest matching
     */
    public static <T> Matcher<T> hasXPaths(final String...xpaths) {
        final Collection<Matcher<? super T>> list =
            new ArrayList<>(xpaths.length);
        for (final String xpath : xpaths) {
            list.add(XhtmlMatchers.<T>hasXPath(xpath));
        }
        return new AllOfThatPrintsOnlyWrongMatchers<T>(list);
    }

    /**
     * Reads an entire reader's contents into a string.
     * @param reader The stream to read
     * @return The reader content, in String form
     */
    private static String readAsString(final Reader reader) {
        @SuppressWarnings("resource")
        final Scanner scanner =
            new Scanner(reader).useDelimiter("\\A");
        final String result;
        try {
            if (scanner.hasNext()) {
                result = scanner.next();
            } else {
                result = "";
            }
        } finally {
            scanner.close();
        }
        return result;
    }
}
