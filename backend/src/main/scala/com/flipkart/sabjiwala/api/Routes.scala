package com.flipkart.sabjiwala.api

import java.io.File

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.flipkart.sabjiwala.directives.FileDirective
import com.flipkart.sabjiwala.wire.{GenericResponse, JsonToEntityMarshaller, Response}

import scala.util.{Failure, Success}
import scala.sys.process._


/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
class Routes extends FileDirective with JsonToEntityMarshaller {

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
              val cmd = s"tesseract ${fileInfo.tmpFilePath} /tmp/out -l eng -c preserve_interword_spaces=1"
              val output = cmd.!!
              val source = scala.io.Source.fromFile("/tmp/out.txt")
              val lines = try source.getLines.toList.filter(_.contains("Rs")) finally source.close()
              complete(GenericResponse(StatusCodes.OK.intValue, null, Response(s"Upload Accepted: Tmp File Created", Map("tmpFile" -> fileInfo.tmpFilePath, "ouput" -> lines))))
            case Failure(e) =>
              //There was some isse processing the fileupload.
              println("Upload File Error", e)
              complete(GenericResponse(StatusCodes.InternalServerError.intValue, null, Response("There was some error processing your request", Map("debug" -> e.getMessage))))
          }

        }
      }
    }


}
