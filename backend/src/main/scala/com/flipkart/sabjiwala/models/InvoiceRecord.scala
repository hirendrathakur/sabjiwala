package com.flipkart.sabjiwala.models

case class InvoiceRecord(invoiceId:String, invoiceDate:String, storeName:String, totalAmount:Double, savings:Double = 0.0)
