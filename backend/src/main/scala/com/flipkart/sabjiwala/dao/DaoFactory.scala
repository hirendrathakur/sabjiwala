package com.flipkart.sabjiwala.dao

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by saurabh.mimani on 23/06/17.
  */
object DaoFactory {

  val mySQLFactory = new MySQLFactory("localhost", "sabjiWalaThela", "root", "", ConfigFactory.empty())

  val invoiceStore = new InvoiceStore("invoiceStore", mySQLFactory)

  val invoiceLineStore = new InvoiceLineStore("invoiceLineStore", mySQLFactory)
}
