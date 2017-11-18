package com.politrons.dsl

import com.twitter.finagle.http.{Method, Response}
import com.twitter.finagle.{Http, http}
import com.twitter.util.Await._

import scalaz.Free.liftF
import scalaz.~>

/**
  * Created by Pablo Perez Garcia on 06/07/2017.
  *
  * This is the main class of the test framework. Using scalaz we create a DSL where the client can use
  * the common Http method request Get, Post, Put, Delete
  *
  * Thanks to this DSL we can separate structure from behaviour of our application.
  *
  * The implementation of this DSL is using the interpreter which apply the logic of the DSL.
  */
object HttpClientDSL extends Actions {

  def Get: ActionMonad[Any] = {
    liftF[Action, Any](_Get())
  }

  def Post: ActionMonad[Any] = {
    liftF[Action, Any](_Post())
  }

  def Put: ActionMonad[Any] = {
    liftF[Action, Any](_Put())
  }

  def Delete: ActionMonad[Any] = {
    liftF[Action, Any](_Delete())
  }

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
      case _To(uri, method) => getRequestInfo(uri, method)
      case _WithBody(body, requestInfo) => requestInfo.request.write(body); result(requestInfo.service(requestInfo.request))
      case _Result(requestInfo) => result(requestInfo.service(requestInfo.request)).getContentString()
      case _isStatus(code, any) => isStatusCodeEqualsThan(code, any)
      case _Status(any) => getstatusCode(any)
      case _ => throw new IllegalArgumentException("No action allowed by the DSL")
    }
  }

  private def getstatusCode[A](any: Any) = {
    any match {
      case ri: RequestInfo => result(ri.service(ri.request)).statusCode
      case response: Response => response.statusCode
      case _ =>
    }
  }

  private def isStatusCodeEqualsThan[A](code: Int, any: Any) = {
    any match {
      case ri: RequestInfo => result(ri.service(ri.request)).statusCode == code
      case response: Response => response.statusCode == code
      case _ =>
    }
  }

  private def getRequestInfo[A](_uri: String, method: Method) = {
    val uri = new java.net.URI(_uri)
    val host = uri.getAuthority
    val path = uri.getPath
    checkUri(host, path)
    if (uri.getScheme == "https") {
      new RequestInfo(Http.client.withTls(host).newService(host), http.Request(method, path))
    } else {
      new RequestInfo(Http.newService(host), http.Request(method, path))
    }
  }

  private def checkUri[A](host: String, path: String) = {
    if (host == null || path == null) throw new IllegalArgumentException("Wrong uri format, be sure to has the structure [[Http/s]//[host]:[port]]")
  }

  implicit class customRequestInfo(requestInfo: RequestInfo) {

    def service = requestInfo._1

    def request = requestInfo._2
  }

}




