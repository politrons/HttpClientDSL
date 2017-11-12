package com.politrons.mock

import com.twitter.finagle._
import com.twitter.finagle.http.service.HttpResponseClassifier
import com.twitter.finagle.http.{Request, Response}

/**
  * Created by pabloperezgarcia on 08/04/2017.
  *
  * Finagle provide multiple operators features on server side that could be handy
  * Such features as retry policy, error handler, max concurrent connections, timeout and so on.
  */
object HttpServers {

  var service: Service[Request, Response] = FinagleService.service

  private val port = "8500"

  def start(): Unit = {
    println("Service up")
    mainServer().serve(s"localhost:$port", service)
  }

  /**
    * This is a regular finagle server
    */
  def mainServer(): Http.Server = {
    Http.server
      .withResponseClassifier(HttpResponseClassifier.ServerErrorsAsFailures)
  }

}
