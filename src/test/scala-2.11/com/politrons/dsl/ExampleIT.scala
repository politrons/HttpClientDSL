package com.politrons.dsl

import org.scalatest.FeatureSpecLike

/**
  * Created by Pablo Perez Garcia on 20/02/2017.
  */
class ExampleIT extends FeatureSpecLike with HttpClientDSL {
  HttpServers.start()
  info("This test is just to prove how the http client DSL works")
  feature("Test http client") {
    scenario(s"Get request to server and response") {
      val result = Get.to("localhost:8500").resultAsString.get
      println(s"Result:$result")
    }

    scenario(s"Get request to server and response status 200") {
      val result = Get.to("localhost:8500").isStatus(200).get
      println(s"Result:$result")
    }
  }
}