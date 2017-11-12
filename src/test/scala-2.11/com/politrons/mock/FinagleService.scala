package com.politrons.mock

import com.twitter.finagle._
import com.twitter.finagle.http.{Method, Request, Response}
import com.twitter.util.Future

/**
  * Created by pabloperezgarcia on 08/04/2017.
  * The Service class will receive and response a Future[Response] with types that you specify
  * Service[Req,Rep]
  */
object FinagleService {

  var body: String = _

  val service: Service[Request, Response] = Service.mk[Request, Response] {
    request: Request => {
      val response = Response()
      request.method match {
        case Method.Get => processGetRequest(request, response)
        case Method.Post | Method.Put => body = request.getContentString(); response.statusCode = 202
        case Method.Delete => body = ""; response.statusCode = 202
      }
      Future.value(response)
    }
  }

  private def processGetRequest(request: Request, response: Response) = {
    request.path match {
      case "/" => response.setContentString(s"Server response:$body".stripMargin)
      case "/home/foo" => response.setContentString(s"Server response:$body".stripMargin)
    }
  }
}
