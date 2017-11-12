Author  Pablo Perez Garcia

![My image](src/main/resources/img/http-icon.png) ![My image](src/main/resources/img/finagle.png)


An open souce library that provide a Http client DSL based on [Free monads](http://eed3si9n.com/learning-scalaz/Free+Monad.html) of [ScalaZ](https://github.com/scalaz/scalaz).
It´s using also [Finagle](https://twitter.github.io/finagle/) toolkit to provide the http client engine.

## Use

* clone the project
```
https://github.com/politrons/HttpClientDSL.git

```
* Run the test example
```
sbt test

```
* Create jar
```
sbt package

```
* Add jar in your project dependency and use object HttpClientDSL
```
target/scala-2.11/httpclientdsl_2.11-1.0.jar

```

## Create your own http client

Using the DSL we can structure our http client:

* Get

```
        Get.to("localhost:8500/home/foo")
          .resultAsString ::
```
* Post

```
        Post.to("localhost:8500")
          .withBody("Hello DSL http client") ::
```

* Put

```
       Put.to("localhost:8500")
          .withBody("Hello DSL http client AGAIN!")
          .isStatus(202) ::
```

* Delete

```
       Delete.to("localhost:8500")
             .isStatus(202) ::
```

You can see the example [here](src/test/scala-2.11/com/politrons/dsl/ExampleIT.scala)







