package com.flipkart.sabjiwala.dao

import com.flipkart.sabjiwala.models.{InvoiceLineRecord, InvoiceRecord}
import org.springframework.dao.DataAccessException

/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
class InvoiceLineStore(table: String, mysqlFactory: MySQLFactory) extends MySQLDao {

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


  def put(data: InvoiceLineRecord): Unit = {
    implicit val j = mysqlHelper.getJDBCInterface
    val q =
      s"""
         |INSERT INTO $table(`invoiceNumber`, `productName`, `originalPrice`, `quantity`, `flipkartPrice`, `creationTS`) VALUES(?, ?, ?, ?, ?, NOW() )
      """.stripMargin
    try {
            update(q, data.invoiceId, data.productName, data.originalPrice, data.quantity, data.flipkartPrice)
    } catch {
      case e: DataAccessException =>
        throw e
    }
  }

}
