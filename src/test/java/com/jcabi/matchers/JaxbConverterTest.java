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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link JaxbConverter}.
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
public final class JaxbConverterTest {

    /**
     * JaxbConverter can convert a JAXB-annotated object to XML.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsJaxbObjectToXml() throws Exception {
        final Object object = new JaxbConverterTest.Employee();
        MatcherAssert.assertThat(
            JaxbConverter.the(object),
            XhtmlMatchers.hasXPath("/employee/name[.='\u0443\u0440\u0430']")
        );
    }

    /**
     * JaxbConverter can convert an object to XML, renderable as a string.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsObjectToSourceRenderableAsText() throws Exception {
        final Object object = new JaxbConverterTest.Employee();
        MatcherAssert.assertThat(
            JaxbConverter.the(object).toString(),
            Matchers.containsString("&#443;")
        );
    }

    /**
     * JaxbConverter can convert an object with other objects injected
     * in runtime.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsAnObjectThatHasOthersInjected() throws Exception {
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

    /**
     * JaxbConverter can convert an object without XmlRootElement annotation.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsNonRootObject() throws Exception {
        final Object object = new JaxbConverterTest.Bar();
        MatcherAssert.assertThat(
            JaxbConverter.the(object),
            XhtmlMatchers.hasXPath("/bar/name")
        );
    }

    /**
     * JaxbConverter can convert an object without XmlRootElement annotation.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void convertsNonRootObjectWithNamespace() throws Exception {
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
