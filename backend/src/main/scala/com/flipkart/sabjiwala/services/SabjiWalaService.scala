package com.flipkart.sabjiwala.services

import com.flipkart.sabjiwala.models.{InvoiceMeta, Invoice}

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object SabjiWalaService {

  def processReciept(file:String): InvoiceMeta = {

    val lines = ImageProcessor(file)
    val bestModel = ParserService("bb")
    val results = bestModel.parse(lines)

    val discount = CatalogService.getDiscount(results)

    InvoiceMeta(
      vendor = results.storeName,
      invoiceDate = discount.invoiceDate,
      invoiceNumber = results.invoiceId,
      amountSaved = discount.totalSavings,
      creationTS = System.currentTimeMillis()
    )

  }
}
