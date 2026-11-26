/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.xml.XMLDocument;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Matcher of XPath against a plain string.
 *
 * <p>Objects of this class are immutable and thread-safe.</p>
 *
 * @param <T> Type of param
 * @since 0.3.7
 */
@ToString
@EqualsAndHashCode(callSuper = false, of = "xpath")
public final class XPathMatcher<T> extends TypeSafeMatcher<T> {

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
     *
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
        return !this.document(input)
            .merge(this.context)
            .nodes(this.xpath)
            .isEmpty();
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("an XML document with XPath ")
            .appendText(this.xpath);
    }

    private XMLDocument document(final T input) {
        final XMLDocument xml = new XMLDocument(XhtmlMatchers.xhtml(input));
        if (!(input instanceof Source) && !(input instanceof Node)
            && !XPathMatcher.PREFIXED.matcher(this.xpath).find()) {
            final Document doc = (Document) xml.inner();
            final NodeList elements = doc.getElementsByTagName("*");
            for (int idx = 0; idx < elements.getLength(); ++idx) {
                final Element element = (Element) elements.item(idx);
                element.removeAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns");
                if (element.getPrefix() == null && element.getNamespaceURI() != null) {
                    doc.renameNode(element, null, element.getLocalName());
                }
            }
        }
        return xml;
    }
}
