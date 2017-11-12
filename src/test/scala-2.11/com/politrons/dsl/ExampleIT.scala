package com.politrons.dsl

import org.scalatest.FeatureSpecLike

/**
  * Created by Pablo Perez Garcia on 20/02/2017.
  */
class ExampleIT extends FeatureSpecLike with HttpClientDSL {

  HttpServers.start()
  info("This test is just to prove how the http client DSL works")
  feature("Test http client") {

    scenario(s"Post request to server and response") {
      println(s"Result:${
        Post
          .to("localhost:8500")
          .withBody("hello DSL http client")
          .fire
      }")
    }

    scenario(s"Get request to server and response") {
      println(s"Result:${
        Get
          .to("localhost:8500")
          .resultAsString
          .fire
      }")
    }

    scenario(s"Get request to server and response status 200") {
      println(s"Result:${
        Get
          .to("localhost:8500")
          .isStatus(200)
          .fire
      }")
    }
  }
}