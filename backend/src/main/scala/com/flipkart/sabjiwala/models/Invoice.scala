package com.flipkart.sabjiwala.models

/**
  * Created by saurabh.mimani on 23/06/17.
  */
case class Invoice (
                   vendor: String,
                   invoiceDate: String,
                   invoiceNumber: String,
                   amountSaved: Double,
                   lastUpdatedTS: Long,
                   creationTS: Long
                   )
