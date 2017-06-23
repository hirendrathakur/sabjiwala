package com.flipkart.sabjiwala.models

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
case class Invoice(invoiceId:String, storeName:String, totalAmount:Double, items:List[InvoiceLine] )
case class InvoiceLine(productName:String, amount:Double, quantity:Double)
