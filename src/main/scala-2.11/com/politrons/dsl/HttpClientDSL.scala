package com.politrons.dsl

import com.twitter.finagle.{Http, http}
import com.twitter.util.Await

import scalaz.~>

/**
  * Created by Pablo Perez Garcia on 06/07/2017.
  *
  * This is the main class of the test framework. Using scalaz we create a DSL where the client can use
  * the common TDD components Given, When, Then.
  *
  * Thanks to this DSL we can separate structure from behaviour of our application.
  *
  * The implementation of this DSL is using the interpreter scenario which apply the logic of the DSL.
  * That interpreter must be implemented by the consumer of the DSL.
  */
trait HttpClientDSL extends Actions {

  /**
    * Using Free monads we need to provide an interpreter that match the algebras used [Action].
    *
    * Here we define that Action type is transformed in Id which is a generic.
    *
    * @return Any possible value defined in the algebra Action´s
    */
  override def interpreter: Action ~> Id = new (Action ~> Id) {
    def apply[A](a: Action[A]): Id[A] = a match {
      case _Get() => http.Method.Get
      case _Post() => http.Method.Post
      case _Delete() => http.Method.Delete
      case _Put() => http.Method.Put
      case _To(uri, method) =>
        val req = http.Request(method, "/")
        val client = Http.newService(uri)
        new RequestInfo(client, req)
      case _WithBody(body, requestInfo) =>
        requestInfo._2.write(body)
        Await.result(requestInfo._1(requestInfo._2))
      case _Result(requestInfo) =>
        Await.result(requestInfo._1(requestInfo._2)).getContentString()
      case _isStatus(code, requestInfo) => Await.result(requestInfo._1(requestInfo._2)).statusCode == code
      case _Status(requestInfo) => Await.result(requestInfo._1(requestInfo._2)).statusCode
      case _ => throw new IllegalArgumentException("No action allowed by the DSL")
    }
  }
}




