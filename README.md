<img src="http://img.jcabi.com/logo-square.png" width="64px" height="64px" />

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-matchers)](http://www.rultor.com/p/jcabi/jcabi-matchers)

[![Build Status](https://travis-ci.org/jcabi/jcabi-matchers.svg?branch=master)](https://travis-ci.org/jcabi/jcabi-matchers)
[![Build status](https://ci.appveyor.com/api/projects/status/1lxligjnsadk2apo/branch/master?svg=true)](https://ci.appveyor.com/project/yegor256/jcabi-matchers/branch/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers)
[![JavaDoc](https://img.shields.io/badge/javadoc-html-blue.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-matchers)

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

## Questions?

If you have any questions about the framework, or something doesn't work as expected,
please [submit an issue here](https://github.com/jcabi/jcabi-matchers/issues/new).

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
