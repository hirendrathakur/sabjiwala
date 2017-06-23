package com.flipkart.sabjiwala.services

import com.flipkart.sabjiwala.models.RawInvoice

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object SabjiWalaService {

  def processReciept(file:String): RawInvoice = {

    val lines = ImageProcessor(file)
    val bestModel = ParserService("bb")
    val results = bestModel.parse(lines)


    results
//    val results = ParserService.parse(fileInfo.tmpFilePath)
//    val ourResults = CatalogService.getDiscount(results)
  }
}
