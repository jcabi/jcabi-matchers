# A Few Matchers for Hamcrest

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/jcabi/jcabi-matchers)](https://www.rultor.com/p/jcabi/jcabi-matchers)

[![mvn](https://github.com/jcabi/jcabi-matchers/actions/workflows/mvn.yml/badge.svg)](https://github.com/jcabi/jcabi-matchers/actions/workflows/mvn.yml)
[![PDD status](https://www.0pdd.com/svg?name=jcabi/jcabi-matchers)](https://www.0pdd.com/p?name=jcabi/jcabi-matchers)
[![Javadoc](https://javadoc.io/badge/com.jcabi/jcabi-matchers.svg)](https://www.javadoc.io/doc/com.jcabi/jcabi-matchers)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers)
[![codecov](https://codecov.io/gh/jcabi/jcabi-matchers/branch/master/graph/badge.svg)](https://codecov.io/gh/jcabi/jcabi-matchers)

More details are here:
[matchers.jcabi.com](http://matchers.jcabi.com/index.html).
Also, read this blog post: [XML/XPath Matchers for Hamcrest][blog].

First, you add this to your `pom.xml`:

```xml
<dependency>
  <groupId>com.jcabi</groupId>
  <artifactId>jcabi-matchers</artifactId>
  <version>1.8.0</version>
</dependency>
```

The library contains a collection of convenient Hamcrest matchers:

```java
import com.jcabi.matchers.XhtmlMatchers;
import org.hamcrest.MatcherAssert;

MatcherAssert.assertThat(
  "<test><name>Jeff</name></test>",
  XhtmlMatchers.hasXPath("/test/name[.='Jeff']")
);
```

To match XHTML documents you need to specify namespaces in your
XPath expressions:

```java
MatcherAssert.assertThat(
  "<!DOCTYPE html><html xmlns='http://www.w3.org/1999/xhtml'><body>Hello, world!</body></html>",
  XhtmlMatchers.hasXPath("/xhtml:html/xhtml:body[.='Hello, world!']")
);
```

Here, we use `xhtml` predefined namespace. There are also
`xsl`, `xs`, `xsi`, and `svg` namespaces
provided off-the-shelf. However, you can define your own too, for example:

```java
MatcherAssert.assertThat(
  "<foo xmlns='my-own-namespace'><bar/></foo>",
  XhtmlMatchers.hasXPath("/ns1:foo/ns1:bar", "my-own-namespace")
);
```

Here, `my-own-namespace` is called `ns1` inside the XPath expression.

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```bash
mvn clean install -Pqulice
```

[blog]: http://www.yegor256.com/2014/04/28/xml-xpath-hamcrest-matchers.html
