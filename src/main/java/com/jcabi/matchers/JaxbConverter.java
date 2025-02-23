/*
 * SPDX-FileCopyrightText: Copyright (c) 2011-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.matchers;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * JAXB-empowered object to XML converting utility.
 *
 * <p>The object has to be annotated with JAXB annotations
 * in order to be convertable. Let's consider an example JAXB-annotated class:
 *
 * <pre> import javax.xml.bind.annotation.XmlAccessType;
 * import javax.xml.bind.annotation.XmlAccessorType;
 * import javax.xml.bind.annotation.XmlElement;
 * import javax.xml.bind.annotation.XmlRootElement;
 * &#64;XmlRootElement(name = "employee")
 * &#64;XmlAccessorType(XmlAccessType.NONE)
 * public class Employee {
 *   &#64;XmlElement(name = "name")
 *   public String getName() {
 *     return "John Doe";
 *   }
 * }</pre>
 *
 * <p>Now you want to test how it works with real data after convertion
 * to XML (in a unit test):
 *
 * <pre> import com.jcabi.matchers.JaxbConverter;
 * import com.jcabi.matchers.XhtmlMatchers;
 * import org.junit.Assert;
 * import org.junit.Test;
 * public final class EmployeeTest {
 *   &#64;Test
 *   public void testObjectToXmlConversion() throws Exception {
 *     final Object object = new Employee();
 *     Assert.assertThat(
 *       JaxbConverter.the(object),
 *       XhtmlMatchers.hasXPath("/employee/name[.='John Doe']")
 *     );
 *   }
 * }</pre>
 *
 * @since 0.1
 */
@ToString
@EqualsAndHashCode
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class JaxbConverter {

    /**
     * Private ctor, to avoid direct instantiation of the class.
     */
    private JaxbConverter() {
        // intentionally empty
    }

    /**
     * Convert an object to XML.
     *
     * <p>The method will throw {@link AssertionError} if marshalling of
     * provided object fails for some reason.
     *
     * <p>The name of the method is motivated by
     * <a href="http://code.google.com/p/xml-matchers/">xmlatchers</a> project
     * and their {@code XmlMatchers.the(String)} method. Looks like this name
     * is short enough and convenient for unit tests.
     *
     * @param object The object to convert
     * @param deps Dependencies that we should take into account
     * @return DOM source/document
     * @throws JAXBException If an exception occurs inside
     */
    public static Source the(final Object object, final Class<?>... deps)
        throws JAXBException {
        final Class<?>[] classes = new Class<?>[deps.length + 1];
        classes[0] = object.getClass();
        System.arraycopy(deps, 0, classes, 1, deps.length);
        final JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(classes);
        } catch (final JAXBException ex) {
            throw new IllegalArgumentException(ex);
        }
        final JAXBIntrospector intro = ctx.createJAXBIntrospector();
        Object subject = object;
        if (intro.getElementName(object) == null) {
            @SuppressWarnings("unchecked")
            final Class<Object> type = (Class<Object>) object.getClass();
            subject = new JAXBElement<Object>(
                JaxbConverter.qname(object),
                type,
                object
            );
        }
        final Marshaller mrsh = JaxbConverter.marshaller(ctx);
        final StringWriter writer = new StringWriter();
        try {
            mrsh.marshal(subject, writer);
        } catch (final JAXBException ex) {
            throw new AssertionError(ex);
        }
        final String xml = writer.toString();
        return new StringSource(xml);
    }

    /**
     * Create marshaller.
     * @param ctx The context
     * @return Marshaller
     * @throws JAXBException If an exception occurs inside
     */
    private static Marshaller marshaller(final JAXBContext ctx)
        throws JAXBException {
        final Marshaller mrsh = ctx.createMarshaller();
        mrsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return mrsh;
    }

    /**
     * Get type name, if XmlType annotation is present (exception otherwise).
     * @param obj The object
     * @return Qualified name
     * @see XmlElement#namespace()
     */
    private static QName qname(final Object obj) {
        final XmlType type = XmlType.class.cast(
            obj.getClass().getAnnotation(XmlType.class)
        );
        if (type == null) {
            throw new AssertionError(
                String.format(
                    // @checkstyle LineLength (1 line)
                    "@XmlType or @XmlRootElement annotation required at %s",
                    obj.getClass().getName()
                )
            );
        }
        final QName qname;
        if ("##default".equals(type.namespace())) {
            qname = new QName(type.name());
        } else {
            qname = new QName(type.namespace(), type.name());
        }
        return qname;
    }

}
