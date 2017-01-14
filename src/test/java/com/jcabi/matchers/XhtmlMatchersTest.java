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

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test case for {@link XhtmlMatchers}.
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class XhtmlMatchersTest {

    /**
     * XhtmlMatchers can match with custom namespace.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void matchesWithCustomNamespace() throws Exception {
        MatcherAssert.assertThat(
            "<a xmlns='foo'><file>abc.txt</file></a>",
            XhtmlMatchers.hasXPath("/ns1:a/ns1:file[.='abc.txt']", "foo")
        );
    }

    /**
     * XhtmlMatchers can report incorrect match.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void doesntMatch() throws Exception {
        MatcherAssert.assertThat(
            "<a/>",
            Matchers.not(XhtmlMatchers.hasXPath("/foo"))
        );
    }

    /**
     * XhtmlMatchers can match against string.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void matchesPlainString() throws Exception {
        MatcherAssert.assertThat(
            "<b xmlns='bar'><file>abc.txt</file></b>",
            XhtmlMatchers.hasXPath("/ns1:b/ns1:file[.='abc.txt']", "bar")
        );
        MatcherAssert.assertThat("<a><b/></a>", XhtmlMatchers.hasXPath("//b"));
    }

    /**
     * XhtmlMatchers can match an InputStream.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void matchesInputStreamAndReader() throws Exception {
        MatcherAssert.assertThat(
            IOUtils.toInputStream(
                "<b><file>foo.txt</file></b>",
                StandardCharsets.UTF_8
            ),
            XhtmlMatchers.hasXPath("/b/file[.='foo.txt']")
        );
        MatcherAssert.assertThat(
            new InputStreamReader(
                IOUtils.toInputStream(
                    "<xx><y/></xx>",
                    StandardCharsets.UTF_8
                ),
                StandardCharsets.UTF_8
            ),
            XhtmlMatchers.hasXPath("/xx/y")
        );
    }

    /**
     * XhtmlMatchers can match after JaxbConverter.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void matchesAfterJaxbConverter() throws Exception {
        MatcherAssert.assertThat(
            JaxbConverter.the(new Foo()),
            XhtmlMatchers.hasXPath("/ns1:foo", Foo.NAMESPACE)
        );
    }

    /**
     * XhtmlMatchers can match against generic type.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void matchesWithGenericType() throws Exception {
        final Foo foo = new Foo();
        MatcherAssert.assertThat(
            foo,
            Matchers.allOf(
                Matchers.hasProperty("abc", Matchers.containsString("some")),
                XhtmlMatchers.<Foo>hasXPath("//c")
            )
        );
    }

    /**
     * XhtmlMatchers can convert text to XML.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsTextToXml() throws Exception {
        MatcherAssert.assertThat(
            StringUtils.join(
                "<html xmlns='http://www.w3.org/1999/xhtml'><body>",
                "<p>\u0443</p></body></html>"
            ),
            XhtmlMatchers.hasXPath("/xhtml:html/xhtml:body/xhtml:p[.='\u0443']")
        );
    }

    /**
     * XhtmlMatchers can convert text to XML, with Unicode inside.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsTextToXmlWithUnicode() throws Exception {
        MatcherAssert.assertThat(
            "<a>\u8514  &#8250;</a>",
            XhtmlMatchers.hasXPath("/a")
        );
    }

    /**
     * XhtmlMatchers can handle processing instructions.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void preservesProcessingInstructions() throws Exception {
        MatcherAssert.assertThat(
            "<?xml version='1.0'?><?pi name='foo'?><a/>",
            XhtmlMatchers.hasXPath(
                "/processing-instruction('pi')[contains(.,'foo')]"
            )
        );
    }

    /**
     * XhtmlMatchers can handle DOCTYPE, which potentially may cause troubles.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void processesDocumentsWithDoctype() throws Exception {
        final String text =
            // @checkstyle StringLiteralsConcatenation (6 lines)
            "<?xml version='1.0'?>"
            + "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN'"
            + " 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"
            + "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>"
            + "<body><p>\u0443\u0440\u0430!</p></body>"
            + "</html>";
        MatcherAssert.assertThat(
            text,
            Matchers.allOf(
                XhtmlMatchers.hasXPath("/*"),
                XhtmlMatchers.hasXPath("//*"),
                XhtmlMatchers.hasXPath(
                    "/xhtml:html/xhtml:body/xhtml:p[.='\u0443\u0440\u0430!']"
                ),
                XhtmlMatchers.hasXPath("//xhtml:p[contains(., '\u0443')]")
            )
        );
    }

    /**
     * XhtmlMatchers can convert W3C Node to XML.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsNodeToXml() throws Exception {
        final Document doc = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument();
        final Element root = doc.createElement("foo-1");
        root.appendChild(doc.createTextNode("boom"));
        doc.appendChild(root);
        MatcherAssert.assertThat(
            doc,
            XhtmlMatchers.hasXPath("/foo-1[.='boom']")
        );
    }

    /**
     * XhtmlMatchers can match group of XPaths.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void hasXPaths() throws Exception {
        MatcherAssert.assertThat(
            "<b><file>def.txt</file><file>ghi.txt</file></b>",
            XhtmlMatchers.hasXPaths(
                "/b/file[.='def.txt']",
                "/b/file[.='ghi.txt']"
            )
        );
    }

    /**
     * This method should only prints wrong xpaths.
     * @throws Exception
     */
    @Test
    public void hasXPathsPrintsOnlyWrongXPaths() {
        try {
            MatcherAssert.assertThat(
                "<b><file>some.txt</file><file>gni.txt</file></b>",
                XhtmlMatchers.hasXPaths(
                    "/b/file[.='some.txt']",
                    "/b/file[.='gnx.txt']",
                    "/b/file[.='gni.txt']"
                )
            );
        } catch (final AssertionError error) {
            MatcherAssert.assertThat(
                error.getMessage(),
                Matchers.hasToString(
                    StringUtils.join(
                        "\nExpected: (an XML document ",
                        "with XPath /b/file[.='gnx.txt'])\n",
                        "     but: an XML document with XPath",
                        " /b/file[.='gnx.txt'] was \"",
                        "<b><file>some.txt</file><file>gni.txt</file></b>\""
                    )
                )
            );
        }
    }

    /**
     * Foo.
     */
    @XmlType(name = "foo", namespace = XhtmlMatchersTest.Foo.NAMESPACE)
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class Foo {
        /**
         * XML namespace.
         */
        public static final String NAMESPACE = "foo-namespace";
        @Override
        public String toString() {
            return "<a><c/></a>";
        }
        /**
         * Property abc.
         * @return Value of abc
         */
        public String getAbc() {
            return "some value";
        }
    }
}
