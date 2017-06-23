package com.flipkart.sabjiwala.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.flipkart.sabjiwala.directives.FileDirective
import com.flipkart.sabjiwala.services.SabjiWalaService
import com.flipkart.sabjiwala.wire.{GenericResponse, JsonToEntityMarshaller, Response}

import scala.util.{Failure, Success}


/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
class Routes(implicit mat: Materializer) extends FileDirective with JsonToEntityMarshaller {

  val route =
    pathSingleSlash {
      get {
        complete {
          complete(GenericResponse(StatusCodes.OK.intValue, null, Response(s"Hello World", null)))
        }
      }

    } ~ path("upload") {
      post {
        extractFormData { postMap =>
          val fileInfo = postMap("file").right.get
          fileInfo.status match {
            case Success(_) =>
              println(s"Upload Complete ${fileInfo.tmpFilePath} ")
              try {
                val ourResults = SabjiWalaService.processReciept(fileInfo.tmpFilePath)
                complete(GenericResponse(StatusCodes.OK.intValue, null, Response(s"Upload Accepted: Tmp File Created", ourResults)))
              }
              catch {
                case e: Exception => {
                  println("printing Error", e)
                  complete(GenericResponse(StatusCodes.BadRequest.intValue, null, Response("There was some error processing your request", Map("debug" -> e.getMessage))))
                }
              }
            case Failure(e) =>
              //There was some isse processing the fileupload.
              println("Upload File Error", e)
              complete(GenericResponse(StatusCodes.InternalServerError.intValue, null, Response("There was some error processing your request", Map("debug" -> e.getMessage))))
          }

        }
      }
    }


}
