package com.flipkart.sabjiwala.services

import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.duration._
import com.flipkart.sabjiwala.utils.StringUtils._

import scala.concurrent.{Await, Future}
import HttpService._
import akka.http.scaladsl.model.headers.RawHeader
import com.flipkart.sabjiwala.models.{BbData, BbProductServiceResponse, Invoice}
import com.flipkart.sabjiwala.utils.StringUtils._

/**
  * Created by saurabh.mimani on 22/06/17.
  */
object CatalogService {

  def search(query: String)(implicit mat: Materializer): List[BbData] = {
    val responseFuture: Future[HttpResponse] =
      Http().singleRequest(HttpRequest(uri = s"https://www.bigbasket.com/productservice/autocomplete/?term=${URLEncoder.encode(query, "UTF-8")}&city_id=1", headers = Seq(RawHeader("Cookie", "_bb_vid=\"Mjk4NjkzOTA4Ng==\";")).asInstanceOf[scala.collection.immutable.Seq[HttpHeader]]))
    val response = Await.result(responseFuture, 30.seconds)
    val stringBody = response.entity.getString

    val bbProductServiceResponse = stringBody.getObj[BbProductServiceResponse]
    bbProductServiceResponse.results.data
  }

  def getDiscount(rawInvoice: Invoice): Invoice = {
//    println("purchasedProducts : " + rawInvoice.items)
    var potentialSavings = 0.0
    val updatedItems = for(product <- rawInvoice.items if product.productName.nonEmpty) yield {
      val result = search(product.productName)
      if (result.length > 0) {
        println(s"${result.length} result for ${product.productName} and price ${product.originalPrice}")
        println(result)
        val closest = result.minBy(v => math.abs(v.price.toDouble - product.originalPrice))
        println(s"closest name ${closest.name} and price ${closest.price.toDouble}")
        potentialSavings = potentialSavings + product.originalPrice - closest.price.toDouble
        product.copy(flipkartPrice = closest.price.toDouble)
      } else
        product
    }
    potentialSavings = BigDecimal(potentialSavings).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    rawInvoice.copy(savings = potentialSavings, items = updatedItems)
  }

}
