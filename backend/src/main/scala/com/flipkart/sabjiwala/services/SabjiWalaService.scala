package com.flipkart.sabjiwala.services

import com.flipkart.sabjiwala.models.Invoice

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object SabjiWalaService {

  def processReciept(file:String): Invoice = {

    val lines = ImageProcessor(file)
    val bestModel = ParserService("bb")
    val results = bestModel.parse(lines)

    val discount = CatalogService.getDiscount(results)

    results.copy(savings = discount.totalSavings, invoiceDate = discount.invoiceDate)
  }
}
