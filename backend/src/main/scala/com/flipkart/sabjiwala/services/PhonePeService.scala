package com.flipkart.sabjiwala.services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import com.flipkart.sabjiwala.services.HttpService._
import com.flipkart.sabjiwala.utils.StringUtils
import com.flipkart.sabjiwala.utils.StringUtils._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object PhonePeService {

  def cashback(userId:String, transactionId:String, amount:Int): Try[String] ={
    val url = "https://api-testing.phonepe.com/apis/payments/v1/merchants/FKRT/cashback"
    val payload =
      s"""
        |{
        | "merchantTransactionId": "$transactionId",
        | "merchantOrderId": "ORD01",
        | "message": "Cashback Example",
        | "campaignId": "GROSCAN",
        | "user": {
        |   "type": "MERCHANT_USER_ID",
        |   "id" : "ACMI7LHKCPRB7RC449EPS24CTU83NLQ4"
        | },
        | "amount": $amount,
        | "currencyCode": "INR"
        |}
      """.stripMargin

    val request = HttpRequest(uri = url,entity = HttpEntity(MediaTypes.`application/json`, payload) , method = HttpMethods.POST)

    val responseFuture = Http().singleRequest(request)
    val response = Await.result(responseFuture, 30.seconds)

    val stringBody = response.entity.getString

    if(response.status.isSuccess())
      Success(stringBody)
    else
      Failure(new Throwable(stringBody))
  }

//   def main(args: Array[String]): Unit = {
//    println(cashback("ACMI7LHKCPRB7RC449EPS24CTU83NLQ4", StringUtils.generateRandomStr(8), 100))
//  }

}
