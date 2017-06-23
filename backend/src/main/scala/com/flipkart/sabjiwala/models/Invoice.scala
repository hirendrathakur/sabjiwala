package com.flipkart.sabjiwala.models

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
case class Invoice(invoiceId:String, invoiceDate:String, storeName:String, totalAmount:Double, items:List[InvoiceLine], savings:Double = 0.0, earning:Double = 0.0)
case class InvoiceLine(productName:String, originalPrice:Double, quantity:Double, flipkartPrice:Double = -1.0)


