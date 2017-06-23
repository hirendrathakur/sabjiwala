package com.flipkart.sabjiwala.dao

import org.springframework.dao.DataAccessException

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
class InvoiceStore(table: String, mysqlFactory: MySQLFactory) extends MySQLDao {

  private val mysqlHelper = mysqlFactory

  def get(key: String): Option[Object] = {
    implicit val j = mysqlHelper.getJDBCInterface
    val q =
      s"""
         |SELECT * FROM $table WHERE `key` = ?
      """.stripMargin
    try {
      query[Object](q, key)
    } catch {
      case e: DataAccessException =>
        throw e
    }
  }

//
//  def put(data: InvoiceMeta): Unit = {
//    implicit val j = mysqlHelper.getJDBCInterface
//    val q =
//      s"""
//         |INSERT INTO $table(`vendor`, `invoiceNumber`, `invoiceDate`, `amountSaved`, `creationTS`, `lastUpdatedTS`) VALUES(?, ?, ?, ?, ?, ?)
//         |ON DUPLICATE KEY UPDATE lastUpdatedTS = ?
//      """.stripMargin
//    try {
//            update(q, data.vendor, data.invoiceNumber, data.invoiceDate, data.amountSaved, data.creationTS, data.lastUpdatedTS, data.lastUpdatedTS)
//    } catch {
//      case e: DataAccessException =>
//        throw e
//    }
//  }

}
