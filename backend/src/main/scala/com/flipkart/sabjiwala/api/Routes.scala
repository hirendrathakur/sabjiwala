package com.flipkart.sabjiwala.api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.flipkart.sabjiwala.directives.FileDirective
import com.flipkart.sabjiwala.services.SabjiWalaService
import com.flipkart.sabjiwala.wire.{GenericResponse, JsonToEntityMarshaller, Response}
import com.flipkart.sabjiwala.services.ConnektService
import scala.util.{Failure, Success}
import com.flipkart.sabjiwala.utils.Wrappers.Try_


/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
class Routes(implicit mat: Materializer) extends FileDirective with JsonToEntityMarshaller {

  val route = withoutRequestTimeout {
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
              //ConnektService.sendPN("ACC14134845961631669","55")
              val ourResults = Try_(SabjiWalaService.processReciept(fileInfo.tmpFilePath)).get
              println(ourResults)
              complete(GenericResponse(StatusCodes.OK.intValue, null, Response(s"Upload Accepted: Tmp File Created", ourResults)))
            case Failure(e) =>
              //There was some isse processing the fileupload.
              println("Upload File Error", e)
              complete(GenericResponse(StatusCodes.InternalServerError.intValue, null, Response("There was some error processing your request", Map("debug" -> e.getMessage))))
          }

        }
      }
    }
  }

}
