/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link JaxbConverter}.
 * @since 0.1
 */
final class JaxbConverterTest {

    @Test
    void convertsJaxbObjectToXml() throws Exception {
        final Object object = new JaxbConverterTest.Employee();
        MatcherAssert.assertThat(
            JaxbConverter.the(object),
            XhtmlMatchers.hasXPath("/employee/name[.='\u0443\u0440\u0430']")
        );
    }

    @Test
    void convertsObjectToSourceRenderableAsText() throws Exception {
        final Object object = new JaxbConverterTest.Employee();
        MatcherAssert.assertThat(
            JaxbConverter.the(object).toString(),
            Matchers.containsString("&#443;")
        );
    }

    @Test
    void convertsAnObjectThatHasOthersInjected() throws Exception {
        final JaxbConverterTest.Employee employee =
            new JaxbConverterTest.Employee();
        employee.inject(new JaxbConverterTest.Foo());
        MatcherAssert.assertThat(
            JaxbConverter.the(employee, JaxbConverterTest.Foo.class),
            XhtmlMatchers.hasXPath(
                "/employee/injected/ns1:name",
                JaxbConverterTest.Foo.NAMESPACE
            )
        );
    }

    @Test
    void convertsNonRootObject() throws Exception {
        final Object object = new JaxbConverterTest.Bar();
        MatcherAssert.assertThat(
            JaxbConverter.the(object),
            XhtmlMatchers.hasXPath("/bar/name")
        );
    }

    @Test
    void convertsNonRootObjectWithNamespace() throws Exception {
        final Object object = new JaxbConverterTest.Foo();
        MatcherAssert.assertThat(
            JaxbConverter.the(object),
            XhtmlMatchers.hasXPath(
                "/ns1:foo/ns1:name", JaxbConverterTest.Foo.NAMESPACE
            )
        );
    }

    /**
     * Dummy test object.
     *
     * @since 0.1
     */
    @XmlRootElement(name = "employee")
    @XmlAccessorType(XmlAccessType.NONE)
    private static final class Employee {
        /**
         * Injected object.
         */
        private transient Object injected = "some text";

        /**
         * Inject an object.
         * @param obj The object to inject
         */
        public void inject(final Object obj) {
            this.injected = obj;
        }

        /**
         * Injected object. This method is not used directly, but is used
         * during JAXB converting of this object into XML, at
         * {@link #convertsAnObjectThatHasOthersInjected()}.
         * @return The object
         */
        @XmlElement
        public Object getInjected() {
            return this.injected;
        }

        /**
         * Returns a simple string. This method is not called directly, but
         * is used in {@code convertsJaxbObjectToXml()} for JAXB converting
         * of the object to XML.
         * @return The text
         */
        @XmlElement(name = "name")
        public String getName() {
            return "\u0443\u0440\u0430";
        }
    }

    /**
     * Dummy test object.
     *
     * @since 0.1
     */
    @XmlType(name = "foo", namespace = JaxbConverterTest.Foo.NAMESPACE)
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class Foo {
        /**
         * XML namespace.
         */
        public static final String NAMESPACE = "foo-namespace";

        /**
         * Simple name.
         * @return The name
         */
        @XmlElement(namespace = JaxbConverterTest.Foo.NAMESPACE)
        public String getName() {
            return "Foo: \u0443\u0440\u0430";
        }
    }

    /**
     * Dummy test object.
     *
     * @since 0.1
     */
    @XmlType(name = "bar")
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class Bar {
        /**
         * Simple name.
         * @return The name
         */
        @XmlElement
        public String getName() {
            return "Bar: \u0443\u0440\u0430";
        }
    }

}
