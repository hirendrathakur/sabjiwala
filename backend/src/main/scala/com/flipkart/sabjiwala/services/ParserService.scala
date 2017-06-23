package com.flipkart.sabjiwala.services

import com.flipkart.sabjiwala.models.{ InvoiceLine, Invoice}
import com.flipkart.sabjiwala.utils.Wrappers._

import scala.util.matching.Regex

trait ParserModel {
  def parse(lines: List[String]): Invoice
}

object ParserService {

  private val parsers = Map(
    "bb" -> BigBasketParserService
  )

  def apply(name: String): ParserModel = parsers(name)
}


private object BigBasketParserService extends ParserModel {

  override def parse(lines: List[String]): Invoice = {
    val productLines = lines.filter(l => l.contains("Rs") && !l.contains("Total") && !l.contains("Payable"))
    val invoiceLine = lines.filter(_.contains("Order ID"))
    getFormatedResponse(productLines, invoiceLine)
  }

  private def getFormatedResponse(lines: List[String], invoiceLine: List[String]): Invoice = {
    val regex1 = ".+?(?=Rs)(.*)".r
    val regex2 = ".+?(?=Rs)".r
    val regex3 = "([0-9]*[.]?[0-9]+)".r
    val invoiceRegex = "(.*) (.*)".r
    val invoiceNumber = try invoiceRegex.findFirstMatchIn(invoiceLine.head).get.group(2).toString catch {case e:Exception=>""}
    val formattedLines = lines.flatMap { line =>
      val group1 = getGroup(regex2, 0, line).toString.trim
      val group2 = getGroup(regex1, 1, line)
      val price = getFirstMatch(regex3, group2)
      val quantity = getSecondMatch(regex3, group2)
      //      List(group1, amount, quantity).mkString(",")
      val r1 = "[A-Za-z]".r
      val firstIdx = group1.indexOfSlice(r1.findFirstIn(group1).getOrElse(""))
      Try_(InvoiceLine( productName = group1.substring(firstIdx) , originalPrice = price.toDouble, quantity = quantity.toDouble )).toOption
    }
    val notNullData = formattedLines.filter(_.productName.nonEmpty)

    Invoice(invoiceId = invoiceNumber,invoiceDate = "", storeName = "BigBasket", totalAmount = 0.0, items = notNullData)
  }

  def getGroup(regex: Regex, key: Int, line: String): String = {
    try {
      regex.findFirstMatchIn(line).get.group(key)
    } catch {
      case e: Exception => ""
    }
  }

  def getFirstMatch(regex: Regex, line: String): String = {
    try {
      val res = regex.findAllMatchIn(line).toList
      res(0).toString()
    } catch {
      case e: Exception => ""
    }
  }

  def getSecondMatch(regex: Regex, line: String): String = {
    try {
      val res = regex.findAllMatchIn(line).toList
      res(1).toString()
    } catch {
      case e: Exception => ""
    }
  }

}


