/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test case for {@link XhtmlMatchers}.
 * @since 0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
final class XhtmlMatchersTest {

    @Test
    void matchesWithCustomNamespace() {
        MatcherAssert.assertThat(
            "should has xpath",
            "<a xmlns='foo'><file>abc.txt</file></a>",
            XhtmlMatchers.hasXPath("/ns1:a/ns1:file[.='abc.txt']", "foo")
        );
    }

    @Test
    void doesntMatch() {
        MatcherAssert.assertThat(
            "should has xpath",
            "<a/>",
            Matchers.not(XhtmlMatchers.hasXPath("/foo"))
        );
    }

    @Test
    void matchesPlainString() {
        MatcherAssert.assertThat(
            "should has xpath",
            "<b xmlns='bar'><file>abc.txt</file></b>",
            XhtmlMatchers.hasXPath("/ns1:b/ns1:file[.='abc.txt']", "bar")
        );
        MatcherAssert.assertThat("should has xpath", "<a><b/></a>", XhtmlMatchers.hasXPath("//b"));
    }

    @Test
    void matchesInputStreamAndReader() {
        MatcherAssert.assertThat(
            "should has xpath",
            IOUtils.toInputStream(
                "<b><file>foo.txt</file></b>",
                StandardCharsets.UTF_8
            ),
            XhtmlMatchers.hasXPath("/b/file[.='foo.txt']")
        );
        MatcherAssert.assertThat(
            "should has xpath",
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

    @Test
    void matchesAfterJaxbConverter() throws Exception {
        MatcherAssert.assertThat(
            "should has xpath",
            JaxbConverter.the(new Foo()),
            XhtmlMatchers.hasXPath("/ns1:foo", Foo.NAMESPACE)
        );
    }

    @Test
    void matchesWithGenericType() {
        final Foo foo = new Foo();
        MatcherAssert.assertThat(
            "should matches all of patterns",
            foo,
            Matchers.allOf(
                Matchers.hasProperty("abc", Matchers.containsString("some")),
                XhtmlMatchers.<Foo>hasXPath("//c")
            )
        );
    }

    @Test
    void convertsTextToXml() {
        MatcherAssert.assertThat(
            "should has xpath",
            StringUtils.join(
                "<html xmlns='http://www.w3.org/1999/xhtml'><body>",
                "<p>\u0443</p></body></html>"
            ),
            XhtmlMatchers.hasXPath("/xhtml:html/xhtml:body/xhtml:p[.='\u0443']")
        );
    }

    @Test
    void convertsTextToXmlWithUnicode() {
        MatcherAssert.assertThat(
            "should has xpath",
            "<a>\u8514  &#8250;</a>",
            XhtmlMatchers.hasXPath("/a")
        );
    }

    @Test
    void preservesProcessingInstructions() {
        MatcherAssert.assertThat(
            "should has xpath",
            "<?xml version='1.0'?><?pi name='foo'?><a/>",
            XhtmlMatchers.hasXPath(
                "/processing-instruction('pi')[contains(.,'foo')]"
            )
        );
    }

    @Test
    void processesDocumentsWithDoctype() {
        final String text =
            // @checkstyle StringLiteralsConcatenation (6 lines)
            "<?xml version='1.0'?>"
                + "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN'"
                + " 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"
                + "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>"
                + "<body><p>\u0443\u0440\u0430!</p></body>"
                + "</html>";
        MatcherAssert.assertThat(
            "should has xpaths",
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

    @Test
    void convertsNodeToXml() throws Exception {
        final Document doc = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument();
        final Element root = doc.createElement("foo-1");
        root.appendChild(doc.createTextNode("boom"));
        doc.appendChild(root);
        MatcherAssert.assertThat(
            "should has xpath",
            doc,
            XhtmlMatchers.hasXPath("/foo-1[.='boom']")
        );
    }

    @Test
    void hasXPaths() {
        MatcherAssert.assertThat(
            "should has xpaths",
            "<b><file>def.txt</file><file>ghi.txt</file></b>",
            XhtmlMatchers.hasXPaths(
                "/b/file[.='def.txt']",
                "/b/file[.='ghi.txt']"
            )
        );
    }

    @Test
    void hasXPathsPrintsOnlyWrongXPaths() {
        try {
            MatcherAssert.assertThat(
                "should has xpaths",
                "<b><file>some.txt</file><file>gni.txt</file></b>",
                XhtmlMatchers.hasXPaths(
                    Arrays.asList(
                        "/b/file[.='some.txt']",
                        "/b/file[.='gnx.txt']",
                        "/b/file[.='gni.txt']"
                    )
                )
            );
        } catch (final AssertionError error) {
            MatcherAssert.assertThat(
                "should contains a string",
                error.getMessage(),
                Matchers.containsString(
                    "Expected: (an XML document with XPath /b/file[.='gnx.txt'])"
                )
            );
        }
    }

    /**
     * Foo.
     *
     * @since 0.1
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
