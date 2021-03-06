# Baqla: Data and Software Evolution

Software evolution framework increases productivity of development teams and makes it easy to evolve software over time.
Baqla allows splitting systems into functional and data parts enabling simultaneous evolution of both.

In a traditional way developers build an application to manage data. With the Baqla development model the focus moves from an application to the data making it the center of a system, with applications built "around" the data.


## Latest Release

The most recent release is 0.0.5 (requires Java 12+)

To add dependency on Baqla use the following

```xml
<dependency>
  <groupId>com.velevedi.baqla</groupId>
  <artifactId>baqla</artifactId>
  <version>0.0.5</version>
</dependency>
```

```gradle
dependencies {
    implementation 'com.velevedi.baqla:baqla:0.0.5'
}
```


## Modules

| Module | Licence | Description |
|--------|---------|-------------|
|__baqla__|Apache 2.0|API and basic implemenation|
|__baqla-jms__|Proprietary*|JMS based implementation|

`*`Please contact us at velevedi@gmail.com if you want to learn more about licensing opportunities and support.

## Links

- [Concepts, User Guide and How To](https://github.com/velevedi/baqla/wiki)
- [API](http://velevedi.com/baqla/0.0.5/apidocs/) 
- [Issue/Request Tracker](https://github.com/velevedi/baqla/issues)
- [The Log: What every software engineer should know about real-time data's unifying abstraction](https://engineering.linkedin.com/distributed-systems/log-what-every-software-engineer-should-know-about-real-time-datas-unifying)
- [Knowledge In IT](http://velevedi.blogspot.co.uk/2016/11/knowledge-in-it.html)

