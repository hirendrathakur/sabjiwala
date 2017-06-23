package com.flipkart.sabjiwala.models

case class InvoiceLineRecord(invoiceId:String, productName:String, originalPrice:Double, quantity:Double, flipkartPrice:Double = -1.0)
