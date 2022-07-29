<img src="http://img.jcabi.com/logo-square.png" width="64px" height="64px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![Managed by Zerocracy](https://www.0crat.com/badge/C3RUBL5H9.svg)](https://www.0crat.com/p/C3RUBL5H9)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-matchers)](http://www.rultor.com/p/jcabi/jcabi-matchers)

[![mvn](https://github.com/jcabi/jcabi-matchers/actions/workflows/mvn.yml/badge.svg)](https://github.com/jcabi/jcabi-matchers/actions/workflows/mvn.yml)
[![PDD status](http://www.0pdd.com/svg?name=jcabi/jcabi-matchers)](http://www.0pdd.com/p?name=jcabi/jcabi-matchers)
[![Javadoc](https://javadoc.io/badge/com.jcabi/jcabi-matchers.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-matchers)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers)
[![codecov](https://codecov.io/gh/jcabi/jcabi-matchers/branch/master/graph/badge.svg)](https://codecov.io/gh/jcabi/jcabi-matchers)

More details are here: [matchers.jcabi.com](http://matchers.jcabi.com/index.html).
Also, read this blog post: [XML/XPath Matchers for Hamcrest](http://www.yegor256.com/2014/04/28/xml-xpath-hamcrest-matchers.html).

The library contains a collection of convenient Hamcrest matchers. For example:

```java
import com.jcabi.matchers.XhtmlMatchers;
import org.hamcrest.MatcherAssert;

MatcherAssert.assertThat(
  "<test><name>Jeff</name></test>",
  XhtmlMatchers.hasXPath("/test/name[.='Jeff']");
);
```

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
