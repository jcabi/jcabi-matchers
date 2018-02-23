<img src="http://img.jcabi.com/logo-square.png" width="64px" height="64px" />

[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![Managed by Zerocracy](https://www.0crat.com/badge/C3RUBL5H9.svg)](https://www.0crat.com/p/C3RUBL5H9)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-matchers)](http://www.rultor.com/p/jcabi/jcabi-matchers)

[![Build Status](https://travis-ci.org/jcabi/jcabi-matchers.svg?branch=master)](https://travis-ci.org/jcabi/jcabi-matchers)
[![PDD status](http://www.0pdd.com/svg?name=jcabi/jcabi-matchers)](http://www.0pdd.com/p?name=jcabi/jcabi-matchers)
[![Build status](https://ci.appveyor.com/api/projects/status/1lxligjnsadk2apo/branch/master?svg=true)](https://ci.appveyor.com/project/yegor256/jcabi-matchers/branch/master)
[![Javadoc](https://javadoc.io/badge/com.jcabi/jcabi-matchers.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-matchers)

[![jpeek report](http://i.jpeek.org/com.jcabi/jcabi-matchers/badge.svg)](http://i.jpeek.org/com.jcabi/jcabi-matchers/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-matchers)
[![Dependencies](https://www.versioneye.com/user/projects/561aa37ea193340f32000fec/badge.svg?style=flat)](https://www.versioneye.com/user/projects/561aa37ea193340f32000fec)

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
