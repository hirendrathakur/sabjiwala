package com.flipkart.sabjiwala.api

import java.io.File

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.flipkart.sabjiwala.directives.FileDirective
import com.flipkart.sabjiwala.services.{CatalogService, ParserService}
import com.flipkart.sabjiwala.utils.StringUtils
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
              val results = ParserService.parse(fileInfo.tmpFilePath)
              val ourResults = CatalogService.getDiscount(results)
//              println("Price and Name from bb")
//              println(bbresults)

              complete(GenericResponse(StatusCodes.OK.intValue, null, Response(s"Upload Accepted: Tmp File Created", Map("tmpFile" -> fileInfo.tmpFilePath, "output" -> ourResults))))
            case Failure(e) =>
              //There was some isse processing the fileupload.
              println("Upload File Error", e)
              complete(GenericResponse(StatusCodes.InternalServerError.intValue, null, Response("There was some error processing your request", Map("debug" -> e.getMessage))))
          }

        }
      }
    }


}
