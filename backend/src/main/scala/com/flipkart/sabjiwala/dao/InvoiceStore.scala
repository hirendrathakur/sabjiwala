package com.flipkart.sabjiwala.dao

import com.flipkart.sabjiwala.models.InvoiceRecord
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


  def put(data: InvoiceRecord): Unit = {
    implicit val j = mysqlHelper.getJDBCInterface
    val q =
      s"""
         |INSERT INTO $table(`vendor`, `invoiceNumber`, `invoiceDate`, `amountSaved`, `creationTS`) VALUES(?, ?, ?, ?, NOW() )
      """.stripMargin
    try {
            update(q, data.storeName, data.invoiceId, data.invoiceDate, data.savings)
    } catch {
      case e: DataAccessException =>
        throw e
    }
  }

}
