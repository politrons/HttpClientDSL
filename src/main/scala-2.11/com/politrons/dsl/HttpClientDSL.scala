package com.politrons.dsl

import com.twitter.conversions.time._
import com.twitter.finagle.http.{Method, Request, Response}
import com.twitter.finagle.param.HighResTimer
import com.twitter.finagle.service.{Backoff, RetryFilter}
import com.twitter.finagle.{Http, Service, http}
import com.twitter.util.Await._
import com.twitter.util.{Return, Try}

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
      case _WithRetry(number, backoff, ri) => new RequestInfo(addRetryPolicy(ri.service, number, backoff), ri.request)
      case _WithBody(body, ri) => ri.request.write(body); result(ri.service(ri.request))
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

  implicit val t = HighResTimer.Default

  def addRetryPolicy(service: Service[Request, Response], number: Int, backoff: Int): Service[Request, Response] = {
    withRetryPolicy(number, backoff).andThen(service)
  }

  def withRetryPolicy(number: Int, backoff: Int): RetryFilter[Request, Response] = {
    RetryFilter(Backoff.const(backoff.millis).take(number))(shouldRetry)
  }

  val shouldRetry: PartialFunction[(Request, Try[Response]), Boolean] = {
    case (_, Return(rep)) => rep.status.code > 404
  }

  private def checkUri[A](host: String, path: String) = {
    if (host == null || path == null) throw new IllegalArgumentException("Wrong uri format, be sure to has the structure [[Http/s]//[host]:[port]]")
  }

  implicit class customRequestInfo(requestInfo: RequestInfo) {

    def service = requestInfo._1

    def request = requestInfo._2
  }

}




