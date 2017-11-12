Author  Pablo Perez Garcia

![My image](src/main/resources/img/finagle.png) ![My image](src/main/resources/img/http-icon.png)


This open souce project provide a Http client DSL based on [Free monads](http://eed3si9n.com/learning-scalaz/Free+Monad.html) of [ScalaZ](https://github.com/scalaz/scalaz).
It´s using also [Finagle](https://twitter.github.io/finagle/) framework to provide the http client engine.

## Use

* clone the project
```
https://github.com/politrons/HttpClientDSL.git

```
* Run the test example
```
sbt test

```

## Create your own http client

Using the DSL we can structure our http client:

* Get

```
        Get
          .to("localhost:8500") ::
```
* Post

```
        Post
          .to("localhost:8500")
          .withBody("Hello DSL http client") ::
```

* Put

```
        Put
          .to("localhost:8500")
          .withBody("Hello DSL http client AGAIN!") ::
```

* Delete

```
       Delete
          .to("localhost:8500")
          .isStatus(202) ::
```







