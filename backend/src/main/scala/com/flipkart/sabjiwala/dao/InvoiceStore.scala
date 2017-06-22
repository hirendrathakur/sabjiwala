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


  def put(data: Object): Unit = {
    implicit val j = mysqlHelper.getJDBCInterface
    val q =
      s"""
         |INSERT INTO $table(`key`, `kind`, `value`, `creationTS`, `lastUpdatedTS`, `expireTS`) VALUES(?, ?, ?, ?, ?, ?)
         |ON DUPLICATE KEY UPDATE value = ?, lastUpdatedTS = ?, expireTS = ?
      """.stripMargin
    try {
      //      update(q, data.keyName, data.kind, data.value, data.creationTS, data.lastUpdatedTS, data.expireTS, data.value, data.lastUpdatedTS, data.expireTS)
    } catch {
      case e: DataAccessException =>
        throw e
    }
  }

}
