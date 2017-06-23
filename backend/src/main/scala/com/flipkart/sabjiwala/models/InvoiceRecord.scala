package com.flipkart.sabjiwala.models

case class InvoiceRecord(id:Integer, invoiceId:String, invoiceDate:String, storeName:String, totalAmount:Double, savings:Double = 0.0)
