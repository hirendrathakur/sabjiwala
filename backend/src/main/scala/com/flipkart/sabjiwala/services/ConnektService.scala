package com.flipkart.sabjiwala.services

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import com.flipkart.sabjiwala.services.HttpService._
import com.flipkart.sabjiwala.utils.StringUtils
import com.flipkart.sabjiwala.utils.StringUtils._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object ConnektService {

  val connektUrl = "http://10.47.0.120/v1/send/push/android/retailapp/users/"

  def id = StringUtils.generateRandomStr(12)

  def sendPN(accId:String, cashBack:String): Try[String] = {
    val payload = s"""{
                      |	"channel": "PN",
                      |	"expiryTs": 1498933800000,
                      |	"channelInfo": {
                      |		"type": "PN",
                      |		"deviceIds": ["8e7feedec40b1ac692ab61c5107a1abc"]
                      |	},
                      |	"sla": "H",
                      |	"contextId": "1E08434G90ALL_USERS",
                      |	"channelData": {
                      |		"type": "PN",
                      |		"data": {
                      |			"notification_type": "Image",
                      |			"priority": "2",
                      |			"appName": "retailBroadcast",
                      |			"uri": "flipkart://fk.dl/de_wv_CL%7CCeryx_--_http%3A%2F%2Fwww.flipkart.com%2Foffers-list%2Fhalf-price-sale~q~screen%3Ddynamic%26pk%3DthemeViews%253DPush-Halfprice%253AmAppDefaultDealCard~widgetType%253DdealCard%26notificationId%3Dtest-16TWS3BH2Z%26omnitureData%3Dtest-16TWS3BH2Z_ME",
                      |			"omnitureData": "test-16TWS3BH2Z_ME",
                      |			"id": "${id}",
                      |			"enableSound": "true",
                      |			"omniture": "test-16TWS3BH2Z_ME",
                      |			"mediaLink": "https://img1a.flixcart.com/www/email/images/20170602-183242-810x450_1_.jpg",
                      |			"message_extras": "You have received a PhonePe cashback of Rs. ${cashBack}",
                      |			"doDismissOnExpire": true,
                      |			"relative_to": "",
                      |			"message": "Grab your favourites at Half the Price! Pick from our super cool range. Just for today, Hurry!",
                      |			"period_time_selector": {
                      |				"type": "STATIC_TIME",
                      |				"params": {
                      |					"expiry": "1498933800000",
                      |					"expiryH": "2017-06-03 23:59:59"
                      |				}
                      |			},
                      |			"expiryH": "2017-06-10 23:59:59",
                      |			"title": "PhonePe CashBack!",
                      |			"type": "",
                      |			"expiry": "1498933800",
                      |			"action": {
                      |				"url": "http://www.flipkart.com/offers-list/half-price-sale?screen=dynamic&pk=themeViews%3DPush-Halfprice%3AmAppDefaultDealCard~widgetType%3DdealCard&notificationId=test-16TWS3BH2Z&omnitureData=test-16TWS3BH2Z_ME",
                      |				"fallback": null,
                      |				"params": {
                      |					"pageKey": "themeViews=Push-Halfprice:mAppDefaultDealCard~widgetType=dealCard",
                      |					"screenName": "dynamic",
                      |					"urlPath": "/offers-list/half-price-sale"
                      |				},
                      |				"screenType": "multiWidgetPage",
                      |				"omnitureData": "test-16TWS3BH2Z_ME",
                      |				"tracking": {
                      |					"omnitureData": "test-16TWS3BH2Z_ME",
                      |					"notificationId": "test-16TWS3BH2Z"
                      |				},
                      |				"loginType": "LOGIN_NOT_REQUIRED",
                      |				"type": "NAVIGATION"
                      |			},
                      |			"icon_image": {
                      |				"320": "",
                      |				"1080": "",
                      |				"480": "",
                      |				"720": "",
                      |				"240": ""
                      |			},
                      |			"summary": "Exciting Offers Inside. Hurry!",
                      |			"doDismissOnClick": "false"
                      |		}
                      |	}
                      |}""".stripMargin
    val request = HttpRequest(uri = s"$connektUrl$accId",entity = HttpEntity(MediaTypes.`application/json`, payload), headers = Seq(RawHeader("x-api-key", "bw3VtGZYhTC4C8NDa94ybX9hf5wXqkuMgZsSRkNUdvNegQC8")).asInstanceOf[scala.collection.immutable.Seq[HttpHeader]], method = HttpMethods.POST)

    val responseFuture = Http().singleRequest(request)
    val response = Await.result(responseFuture, 30.seconds)

    val stringBody = response.entity.getString

    if(response.status.isSuccess()) {
      println(s"Pn Sent :  $stringBody")
      Success(stringBody)
    } else
      Failure(new Throwable(stringBody))
  }

}
