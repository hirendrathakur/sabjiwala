package com.flipkart.sabjiwala.services

import java.io.File
import java.nio.file.{Files, StandardCopyOption}
import java.util.Date

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
    val accid = "ACC14134845961631669" // ACMI7LHKCPRB7RC449EPS24CTU83NLQ4

    println("Scan Results: " + results)

    val updatedInvoice = CatalogService.getDiscount(results)
    val cashbackAmount =  math.abs((updatedInvoice.savings * 100 /10).toInt)

    if(cashbackAmount > 0 ) {
      val cashback = PhonePeService.cashback(accid, StringUtils.generateRandomStr(8), cashbackAmount)
      println(cashback)
      ConnektService.sendPN(accid,(cashbackAmount/100).toString)
    }
    Try_ {
      moveFile(file, updatedInvoice.invoiceId)
      for (item <- updatedInvoice.items) {
        DaoFactory.invoiceLineStore.put(InvoiceLineRecord(updatedInvoice.invoiceId, item.productName, item.originalPrice, item.quantity, item.flipkartPrice))
      }
      DaoFactory.invoiceStore.put(InvoiceRecord(updatedInvoice.invoiceId, "2017-06-23", "Bigbasket", 0.0, updatedInvoice.savings))
    }
    updatedInvoice.copy(earning = cashbackAmount/100.0)
  }

  def moveFile(file:String, invoiceNumber:String): Unit = {
    val username = System.getProperty("user.name")
    val directoryName = s"/Users/${username}/invoices/"
    val fileName = invoiceNumber + "-" + new Date().getTime() + file.substring(file.lastIndexOf('.'))
    println(s"in moveFile ${directoryName}${fileName}")

    val directory = new File(directoryName)
    if (!directory.exists) {
      directory.mkdir
    }
    val dst = new File(s"${directoryName}${fileName}").toPath
    val src = new File(file).toPath
    Files.move(src, dst, StandardCopyOption.ATOMIC_MOVE)
    println("Moved Files")
  }
}
