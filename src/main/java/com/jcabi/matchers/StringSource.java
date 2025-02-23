/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
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
@SuppressWarnings(
    {
        "PMD.OnlyOneConstructorShouldDoInitialization",
        "PMD.ConstructorOnlyInitializesOrCallOtherConstructors"
    }
)
final class StringSource extends DOMSource {

    /**
     * The XML itself.
     */
    private final transient String xml;

    /**
     * Public ctor.
     * @param text The content of the document
     */
    StringSource(final String text) {
        super();
        this.xml = text;
        super.setNode(new XMLDocument(text).node());
    }

    /**
     * Public ctor.
     * @param node The node
     */
    StringSource(final Node node) {
        super();
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
        this.xml = writer.toString();
        this.setNode(node);
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
}
