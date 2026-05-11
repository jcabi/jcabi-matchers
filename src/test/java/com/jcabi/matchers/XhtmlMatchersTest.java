/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
            "should matches with custom namespace",
            "<a xmlns='foo'><file>abc.txt</file></a>",
            XhtmlMatchers.hasXPath("/ns1:a/ns1:file[.='abc.txt']", "foo")
        );
    }

    @Test
    void doesntMatch() {
        MatcherAssert.assertThat(
            "should does not match",
            "<a/>",
            Matchers.not(XhtmlMatchers.hasXPath("/foo"))
        );
    }

    @Test
    void matchesWithoutPrefixWhenDefaultNamespace() {
        MatcherAssert.assertThat(
            "should match unprefixed XPath against element in default namespace",
            "<a xmlns='foo'><b/></a>",
            XhtmlMatchers.hasXPath("/a/b")
        );
    }

    @Test
    void matchesWithoutPrefixForXhtmlDefaultNamespace() {
        MatcherAssert.assertThat(
            "should match unprefixed XPath against XHTML in default namespace",
            StringUtils.join(
                "<html xmlns='http://www.w3.org/1999/xhtml'>",
                "<body><p>hello</p></body></html>"
            ),
            XhtmlMatchers.hasXPath("/html/body/p[.='hello']")
        );
    }

    @Test
    void matchesWithoutPrefixForXhtmlInputStream() {
        MatcherAssert.assertThat(
            "should match unprefixed XPath when input is an InputStream",
            IOUtils.toInputStream(
                "<root xmlns='foo'><child>x</child></root>",
                StandardCharsets.UTF_8
            ),
            XhtmlMatchers.hasXPath("/root/child[.='x']")
        );
    }

    @Test
    void matchesWithoutPrefixForXhtmlReader() {
        MatcherAssert.assertThat(
            "should match unprefixed XPath when input is a Reader",
            new InputStreamReader(
                IOUtils.toInputStream(
                    "<root xmlns='bar'><child>y</child></root>",
                    StandardCharsets.UTF_8
                ),
                StandardCharsets.UTF_8
            ),
            XhtmlMatchers.hasXPath("/root/child[.='y']")
        );
    }

    @Test
    void prefixedXPathStillMatchesDefaultNamespace() {
        MatcherAssert.assertThat(
            "should let prefixed XPath match elements in a default namespace",
            "<messages xmlns='http://n.validator.nu/messages/'><info/></messages>",
            XhtmlMatchers.hasXPath(
                "//ns1:messages/ns1:info",
                "http://n.validator.nu/messages/"
            )
        );
    }

    @Test
    void matchesPlainStringWithNamespace() {
        MatcherAssert.assertThat(
            "should has xpath",
            "<b xmlns='bar'><file>abc.txt</file></b>",
            XhtmlMatchers.hasXPath("/ns1:b/ns1:file[.='abc.txt']", "bar")
        );
    }

    @Test
    void matchesPlainStringWithoutNamespace() {
        MatcherAssert.assertThat(
            "should has xpath",
            "<a><b/></a>",
            XhtmlMatchers.hasXPath("//b")
        );
    }

    @Test
    void matchesInputStream() {
        MatcherAssert.assertThat(
            "should matches input stream",
            IOUtils.toInputStream(
                "<b><file>foo.txt</file></b>",
                StandardCharsets.UTF_8
            ),
            XhtmlMatchers.hasXPath("/b/file[.='foo.txt']")
        );
    }

    @Test
    void matchesReader() {
        MatcherAssert.assertThat(
            "should matches reader",
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
            "should matches after jaxb converter",
            JaxbConverter.the(new XhtmlMatchersTest.Foo()),
            XhtmlMatchers.hasXPath("/ns1:foo", XhtmlMatchersTest.Foo.NAMESPACE)
        );
    }

    @Test
    void matchesWithGenericType() {
        MatcherAssert.assertThat(
            "should matches all of patterns",
            new XhtmlMatchersTest.Foo(),
            Matchers.allOf(
                Matchers.hasProperty("abc", Matchers.containsString("some")),
                XhtmlMatchers.<XhtmlMatchersTest.Foo>hasXPath("//c")
            )
        );
    }

    @Test
    void convertsTextToXml() {
        MatcherAssert.assertThat(
            "should converts text to xml",
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
            "should converts text to xml with unicode",
            "<a>\u8514  &#8250;</a>",
            XhtmlMatchers.hasXPath("/a")
        );
    }

    @Test
    void preservesProcessingInstructions() {
        MatcherAssert.assertThat(
            "should preserves processing instructions",
            "<?xml version='1.0'?><?pi name='foo'?><a/>",
            XhtmlMatchers.hasXPath(
                "/processing-instruction('pi')[contains(.,'foo')]"
            )
        );
    }

    @Test
    void processesDocumentsWithDoctype() {
        MatcherAssert.assertThat(
            "should processes documents with doctype",
            // @checkstyle StringLiteralsConcatenation (6 lines)
            "<?xml version='1.0'?>"
                + "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN'"
                + " 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>"
                + "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>"
                + "<body><p>\u0443\u0440\u0430!</p></body>"
                + "</html>",
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
            "should converts node to xml",
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
        final org.hamcrest.Matcher<String> matcher = XhtmlMatchers.hasXPaths(
            Arrays.asList(
                "/b/file[.='some.txt']",
                "/b/file[.='gnx.txt']",
                "/b/file[.='gni.txt']"
            )
        );
        matcher.matches("<b><file>some.txt</file><file>gni.txt</file></b>");
        final org.hamcrest.StringDescription description =
            new org.hamcrest.StringDescription();
        matcher.describeTo(description);
        MatcherAssert.assertThat(
            "should contain wrong xpath in error message",
            description.toString(),
            Matchers.containsString("an XML document with XPath /b/file[.='gnx.txt']")
        );
    }

    /**
     * Foo.
     * @since 0.1
     */
    @XmlType(name = "foo", namespace = XhtmlMatchersTest.Foo.NAMESPACE)
    @XmlAccessorType(XmlAccessType.NONE)
    @SuppressWarnings("PMD.PublicMemberInNonPublicType")
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
