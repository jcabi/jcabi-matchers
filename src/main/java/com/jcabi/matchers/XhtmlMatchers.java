/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.xml.XPathContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
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
            source = (Source) xhtml;
        } else if (xhtml instanceof InputStream) {
            try (InputStream stream = (InputStream) xhtml) {
                source = new StringSource(
                    readAsString(new InputStreamReader(stream, StandardCharsets.UTF_8))
                );
            } catch (final IOException ex) {
                throw new IllegalStateException(ex);
            }
        } else if (xhtml instanceof Reader) {
            try (Reader reader = (Reader) xhtml) {
                source = new StringSource(readAsString(reader));
            } catch (final IOException ex) {
                throw new IllegalStateException(ex);
            }
        } else if (xhtml instanceof Node) {
            source = new StringSource((Node) xhtml);
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
    public static <T> Matcher<T> hasXPaths(final String... xpaths) {
        return XhtmlMatchers.hasXPaths(Arrays.asList(xpaths));
    }

    /**
     * Matches content against list of XPaths.
     * @param xpaths The query
     * @param <T> Type of XML content provided
     * @return Matcher suitable for JUnit/Hamcrest matching
     * @since 1.8.0
     */
    public static <T> Matcher<T> hasXPaths(final Iterable<String> xpaths) {
        final Collection<Matcher<? super T>> list = new LinkedList<>();
        for (final String xpath : xpaths) {
            list.add(XhtmlMatchers.hasXPath(xpath));
        }
        return new AllOfThatPrintsOnlyWrongMatchers<>(list);
    }

    /**
     * Reads an entire reader's contents into a string.
     * @param reader The stream to read
     * @return The reader content, in String form
     */
    private static String readAsString(final Reader reader) {
        final String result;
        try (Scanner scanner = new Scanner(reader).useDelimiter("\\A")) {
            if (scanner.hasNext()) {
                result = scanner.next();
            } else {
                result = "";
            }
        }
        return result;
    }
}
