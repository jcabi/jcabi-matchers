/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import com.jcabi.xml.XMLDocument;
import java.io.StringWriter;
import java.util.Locale;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.EqualsAndHashCode;
import org.w3c.dom.Node;

/**
 * Private class for DOM to String converting.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = false, of = "xml")
final class StringSource extends DOMSource {

    /**
     * The XML itself.
     */
    private final transient String xml;

    /**
     * Public ctor.
     * @param node The node
     * @checkstyle ConstructorsCodeFreeCheck (4 lines)
     */
    StringSource(final Node node) {
        this(node, StringSource.serialize(node));
    }

    /**
     * Public ctor.
     * @param text The content of the document
     * @checkstyle ConstructorsCodeFreeCheck (4 lines)
     */
    StringSource(final String text) {
        this(new XMLDocument(text).deepCopy(), text);
    }

    /**
     * Private primary ctor.
     * @param node The node
     * @param text The XML text
     */
    private StringSource(final Node node, final String text) {
        super(node);
        this.xml = text;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        final int length = this.xml.length();
        for (int pos = 0; pos < length; ++pos) {
            final char chr = this.xml.charAt(pos);
            // @checkstyle MagicNumber (1 line)
            if (chr > 0x7f) {
                buf.append("&#").append(
                    Integer.toHexString(chr).toUpperCase(Locale.ENGLISH)
                ).append(';');
            } else {
                buf.append(chr);
            }
        }
        return buf.toString();
    }

    /**
     * Serialize a DOM node to XML string.
     * @param node The node to serialize
     * @return XML representation
     */
    private static String serialize(final Node node) {
        final StringWriter writer = new StringWriter();
        try {
            final Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
            final String yes = "yes";
            transformer.setOutputProperty(
                OutputKeys.OMIT_XML_DECLARATION, yes
            );
            transformer.setOutputProperty(OutputKeys.INDENT, yes);
            transformer.transform(
                new DOMSource(node),
                new StreamResult(writer)
            );
        } catch (final TransformerException ex) {
            throw new IllegalStateException(ex);
        }
        return writer.toString();
    }
}
