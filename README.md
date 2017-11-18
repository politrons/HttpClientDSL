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
* Add jar in your project dependency and use object factory HttpClientDSL
```
target/scala-2.11/httpclientdsl_2.11-1.0.jar

```

## Create your own http client

Using the DSL we can structure our http client with this options.

* We can make http and https request.
* We can get response body, response status, and check this last one with the code we pass.
* Also we have implemented a retry polciy where you can pass the number of retries and backoff between every retry.

### DSL Examples

* Get

```
        Get.to("http://localhost:8500/home/foo")
          .resultAsString ::
```
* Post

```
        Post.to("http://localhost:8500")
          .withBody("Hello DSL http client") ::
```

* Put

```
       Put.to("http://localhost:8500")
          .withBody("Hello DSL http client AGAIN!")
          .isStatus(202) ::
```

* Delete

```
       Delete.to("http://localhost:8500")
             .isStatus(202) ::
```

* Retry policy

```
      Get.to("http://localhost:8500/home/foo")
          .withRetry(number = 7, backoff = 500)
          .resultAsString ::


     Post.to("http://localhost:8500")
              .withRetry(number = 7, backoff = 500)
              .withBody("Hello DSL http client") ::
```

You can see the example [here](src/test/scala-2.11/com/politrons/dsl/ExampleIT.scala)







