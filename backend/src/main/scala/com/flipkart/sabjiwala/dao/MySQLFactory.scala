package com.flipkart.sabjiwala.dao

import java.util.Properties
import javax.sql.DataSource

import com.typesafe.config.Config
import org.apache.commons.dbcp2.BasicDataSourceFactory
import org.springframework.jdbc.core.JdbcTemplate


/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
class MySQLFactory private(config: Properties) {

  private val source: DataSource =  BasicDataSourceFactory.createDataSource(config)

  def this(host: String, database: String, username: String, password: String, poolProps: Config) = {
    this(PropsHelper.getConnProperties(host, database, username, password, poolProps))
  }

  def getJDBCInterface: JdbcTemplate = new JdbcTemplate(source)
}

object PropsHelper {
  val connUrl = "jdbc:mysql://%s/%s?autoReconnect=true&useUnicode=true&characterEncoding=utf-8"
  val driverClassName = "com.mysql.jdbc.Driver"

  def getConnProperties(host: String, database: String, username: String, password: String, poolProps: Config): Properties = {
    val connProps = new Properties()
    connProps.setProperty("url", connUrl.format(host, database))
    connProps.setProperty("driverClassName", driverClassName)
    connProps.setProperty("username", username)
    connProps.setProperty("password", password)
    connProps.setProperty("maxIdle", "100")
    connProps.setProperty("initialSize", "10")
    connProps.setProperty("maxActive", "100")
    connProps.setProperty("autoReconnect", "true")
    connProps.setProperty("testOnBorrow", "true")
    connProps.setProperty("testOnReturn", "false")
    connProps.setProperty("validationQuery", "select 1")
    connProps.setProperty("validationQueryTimeout", "2000")
    connProps
  }
}
