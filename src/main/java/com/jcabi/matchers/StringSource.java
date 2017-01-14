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
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
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
