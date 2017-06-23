package com.flipkart.sabjiwala.services

import com.flipkart.sabjiwala.dao.DaoFactory
import com.flipkart.sabjiwala.models.{Invoice, InvoiceLineRecord, InvoiceRecord}
import com.flipkart.sabjiwala.utils.StringUtils

import com.flipkart.sabjiwala.utils.Wrappers._
/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object SabjiWalaService {

  def processReciept(file:String): Invoice = {

    val lines = ImageProcessor(file)
    val bestModel = ParserService("bb")
    val results = bestModel.parse(lines)
    val accid = "ACC14134845961631669"
    val updatedInvoice = CatalogService.getDiscount(results)
    val cashbackAmount =  math.abs((updatedInvoice.savings * 100 /10).toInt)

    if(cashbackAmount > 0 ) {
      val cashback = PhonePeService.cashback(accid, StringUtils.generateRandomStr(8), cashbackAmount)
      println(cashback)
      ConnektService.sendPN(accid,(cashbackAmount/100).toString)
    }

    Try_ {
      for (item <- updatedInvoice.items) {
        DaoFactory.invoiceLineStore.put(InvoiceLineRecord(updatedInvoice.invoiceId, item.productName, item.originalPrice, item.quantity, item.flipkartPrice))
      }

      DaoFactory.invoiceStore.put(InvoiceRecord(updatedInvoice.invoiceId, "2017-06-23", "Bigbasket", 0.0, updatedInvoice.savings))
    }
    updatedInvoice.copy(earning = cashbackAmount/100.0)
  }
}
