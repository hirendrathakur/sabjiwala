package com.flipkart.sabjiwala.services

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object SabjiWalaService {

  def processReciept(file:String)= {

    val lines = ImageProcessor(file)
    val bestModel = ParserService("bb")
    val results = bestModel.parse(lines)



//    val results = ParserService.parse(fileInfo.tmpFilePath)
//    val ourResults = CatalogService.getDiscount(results)
  }
}
