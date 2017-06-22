package com.flipkart.sabjiwala.services

import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.duration._
import com.flipkart.sabjiwala.utils.StringUtils._

import scala.concurrent.{Await, Future}
import HttpService.actorSystem
import akka.http.scaladsl.model.headers.RawHeader
import com.flipkart.sabjiwala.models.{BbData, BbProductServiceResponse}
import com.flipkart.sabjiwala.utils.StringUtils._

/**
  * Created by saurabh.mimani on 22/06/17.
  */
object CatalogService {

  implicit val materializer = ActorMaterializer()

  def search(query: String)(implicit mat: Materializer): List[BbData] = {
    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(uri = s"https://www.bigbasket.com/productservice/autocomplete/?term=${URLEncoder.encode(query, "UTF-8")}&city_id=1", headers = Seq(RawHeader("Cookie", "_bb_vid=\"Mjk4NjkzOTA4Ng==\";")).asInstanceOf[scala.collection.immutable.Seq[HttpHeader]]))
    val response = Await.result(responseFuture, 30.seconds)
    val stringBody = response.entity.getString

    val bbProductServiceResponse = stringBody.getObj[BbProductServiceResponse]
    bbProductServiceResponse.results.data
  }

  def getDiscount(purchasedPrdcts: Map[String, Any]): UploadResponse = {
    println("purchasedProducts")
    val purchasedProducts = purchasedPrdcts.get("metaData").asInstanceOf[List[Map[String, Any]]]
    println(purchasedProducts)
    var potentialSavings = 0.0
    for(product <- purchasedProducts if product.nonEmpty && product("name").toString.nonEmpty) {
      val result = search(product("name").asInstanceOf[String])
      if(result.length > 0){
        println(s"result for ${product("name").asInstanceOf[String]} and price ${product("price")}")
        println(result)
        val closest = result.minBy(v => math.abs(v.price.toDouble - product("price").toString.toDouble))
        println(closest)
        potentialSavings = potentialSavings + product("price").toString.toDouble - closest.price.toDouble
      }
    }
    val invoiceNumber = purchasedPrdcts.get("invoiceId").toString
    val uploadResponse = UploadResponse(invoiceNumber, "10-June-2017", potentialSavings)
    potentialSavings = BigDecimal(potentialSavings).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    return uploadResponse
  }

  case class UploadResponse(
                            invoiceNo: String,
                            invoiceDate: String,
                            totalSavings: Double
                           )
}
