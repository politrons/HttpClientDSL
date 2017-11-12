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
      println(s"Post result:${
        Post
          .to("localhost:8500")
          .withBody("Hello DSL http client")
          .fire
      }")
    }

    scenario(s"Get request to server and response") {
      println(s"Get result:${
        Get
          .to("localhost:8500")
          .resultAsString
          .fire
      }")
    }

    scenario(s"Put request to server and response") {
      println(s"Put result:${
        Put
          .to("localhost:8500")
          .withBody("Hello DSL http client AGAIN!")
          .fire
      }")
    }

    scenario(s"Get request to server and response is modify") {
      println(s"Get result:${
        Get
          .to("localhost:8500")
          .resultAsString
          .fire
      }")
    }

    scenario(s"Delete request to server and response") {
      println(s"Delete status code 202:${
        Delete
          .to("localhost:8500")
          .isStatus(202)
          .fire
      }")
    }

    scenario(s"Get request to server and response message is empty") {
      println(s"Get result:${
        Get
          .to("localhost:8500")
          .resultAsString
          .fire
      }")
    }


    scenario(s"Get request to server and response status 200") {
      println(s"Get status code 200:${
        Get
          .to("localhost:8500")
          .isStatus(200)
          .fire
      }")
    }
  }
}