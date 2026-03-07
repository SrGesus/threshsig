# Threshsig

To add as maven dependency, you can add the jitpack repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>com.github.srgesus</groupId>
        <artifactId>threshsig</artifactId>
        <version>0.0.1</version>
    </dependency>
</dependencies>
```

This is an implementation of Victor Shoup's ["Practical Threshold Signatures"]
(http://www.shoup.net/papers/thsig.pdf). It was originally written as an
undergraduate summer project for [Oceanstore](http://oceanstore.cs.berkeley.edu/) in 2000.

**Warning**: This code is not maintained, does not use modern coding practices, and has no security claims. 

It is only posted as a reference.

A prototype implementation in Python from 2019 is here: https://github.com/oreparaz/shoup
