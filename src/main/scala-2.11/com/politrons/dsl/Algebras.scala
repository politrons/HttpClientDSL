package com.politrons.dsl

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Method, Request, Response}

import scalaz.Free

/**
  * Created by Pablo Perez Garcia on 07/07/2017.
  *
  * Here we define the algebras to be used as part of the DSL.
  *
  * All them we consider as Actions which we define the entry data and return type defined in the Action.
  */
trait Algebras {

  type Id[+A] = A

  sealed trait Action[A]

  type ActionMonad[A] = Free[Action, A]

  type RequestInfo = (Service[Request, Response], Request)

  case class _Get() extends Action[Any]

  case class _Post() extends Action[Any]

  case class _Put() extends Action[Any]

  case class _Delete() extends Action[Any]

  case class _To(uri: String, method: Method) extends Action[Any]

  case class _WithBody(body: String, requestInfo: RequestInfo) extends Action[Any]

  case class _WithRetry(number: Int,backoff:Int,  requestInfo: RequestInfo) extends Action[Any]

  case class _Result(requestInfo: RequestInfo) extends Action[Any]

  case class _isStatus(code: Int, any: Any) extends Action[Any]

  case class _Status(any: Any) extends Action[Any]

}
