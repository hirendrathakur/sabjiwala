package com.flipkart.sabjiwala.models

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
case class Invoice(invoiceId:String, storeName:String, totalAmount:Double, items:List[InvoiceLine])
case class InvoiceLine(productName:String, originalPrice:Double, quantity:Double, flipkartPrice:Double = 0.0)

case class InvoiceMeta(
                   vendor: String,
                   invoiceDate: String,
                   invoiceNumber: String,
                   amountSaved: Double,
                   creationTS: Long
                   )
