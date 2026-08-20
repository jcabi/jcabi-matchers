/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.xml.XMLDocument;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Node;

/**
 * Matcher of XPath against a plain string.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @param <T> Type of param
 * @since 0.3.7
 */
@ToString
@EqualsAndHashCode(callSuper = false, of = "xpath")
public final class XPathMatcher<T> extends TypeSafeMatcher<T> {

    /**
     * Default-namespace declaration ({@code xmlns="..."}) pattern, but not
     * a prefixed declaration like {@code xmlns:foo="..."}.
     */
    private static final Pattern DEFAULT_NS = Pattern.compile(
        "\\s+xmlns\\s*=\\s*(\"[^\"]*\"|'[^']*')"
    );

    /**
     * Detects a prefixed name in an XPath query (e.g. {@code ns1:foo}),
     * while ignoring the {@code ::} XPath axis separator.
     */
    private static final Pattern PREFIXED = Pattern.compile(
        "[A-Za-z_][A-Za-z0-9_.-]*:[A-Za-z_]"
    );

    /**
     * The XPath to use.
     */
    private final transient String xpath;

    /**
     * The context to use.
     */
    private final transient NamespaceContext context;

    /**
     * Public ctor.
     * @param query The query
     * @param ctx The context
     */
    public XPathMatcher(final String query, final NamespaceContext ctx) {
        super();
        this.xpath = query;
        this.context = ctx;
    }

    @Override
    public boolean matchesSafely(final T input) {
        return !new XMLDocument(this.source(input))
            .merge(this.context)
            .nodes(this.xpath)
            .isEmpty();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("an XML document with XPath ")
            .appendText(this.xpath);
    }

    private Source source(final T input) {
        final Source result;
        if (XPathMatcher.PREFIXED.matcher(this.xpath).find()
            || input instanceof Source
            || input instanceof Node) {
            result = XhtmlMatchers.xhtml(input);
        } else {
            result = new StringSource(
                XPathMatcher.DEFAULT_NS.matcher(XPathMatcher.asText(input))
                    .replaceAll("")
            );
        }
        return result;
    }

    private static String asText(final Object input) {
        final String text;
        if (input instanceof InputStream) {
            text = XPathMatcher.read(
                new InputStreamReader(
                    (InputStream) input,
                    StandardCharsets.UTF_8
                )
            );
        } else if (input instanceof Reader) {
            text = XPathMatcher.read((Reader) input);
        } else {
            text = input.toString();
        }
        return text;
    }

    private static String read(final Reader reader) {
        final StringWriter writer = new StringWriter();
        try {
            reader.transferTo(writer);
        } catch (final IOException ex) {
            throw new IllegalStateException("Failed to read the source", ex);
        }
        return writer.toString();
    }
}
