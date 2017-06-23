package com.flipkart.sabjiwala.services

import com.flipkart.sabjiwala.models.{Invoice, RawInvoice}

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object SabjiWalaService {

  def processReciept(file:String): Invoice = {

    val lines = ImageProcessor(file)
    val bestModel = ParserService("bb")
    val results = bestModel.parse(lines)

    val discount = CatalogService.getDiscount(results)

    Invoice(
      vendor = results.storeName,
      invoiceDate = "today",
      invoiceNumber = results.invoiceId,
      amountSaved = discount.totalSavings,
      creationTS = System.currentTimeMillis()
    )

  }
}
