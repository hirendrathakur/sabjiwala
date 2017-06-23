package com.flipkart.sabjiwala.services

import com.flipkart.sabjiwala.dao.DaoFactory
import com.flipkart.sabjiwala.models.InvoiceRecord

/**
  * Created by saurabh.mimani on 23/06/17.
  */
object Validator {
  def validate(invoiceNumber:String): Boolean = {
    println(s"invoiceNumber is ${invoiceNumber}")
    DaoFactory.invoiceStore.get(invoiceNumber) match {
      case Some(ir: InvoiceRecord) => throw new Exception("Duplicate Invoice")
      case _ => true
    }
  }
}
