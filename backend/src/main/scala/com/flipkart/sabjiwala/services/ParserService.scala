package com.flipkart.sabjiwala.services

import java.io.File

import com.flipkart.sabjiwala.models.{ InvoiceLine, Invoice}
import com.flipkart.sabjiwala.utils.Wrappers._

import scala.io.Source
import scala.util.Try
import scala.util.matching.Regex

trait ParserModel {
  def parse(lines: List[String]): Invoice
}

object ParserService {

  private val parsers = Map(
    "bb" -> BigBasketParserService,
  "generic" -> DefaultParserService
  )

  def apply(name: String): ParserModel = parsers(name)
}


private object DefaultParserService extends ParserModel {

  val entryRegex = """(.*)(\s{10,})(.*)""".r
  val priceRegex = """(.*) ([0-9-.]+)""".r

  override def parse(lines: List[String]): Invoice = {
    val priceLines = lines.flatMap(line => {
      val d = entryRegex.findFirstMatchIn(line)
      d.filter(groups => {
        groups.groupCount == 3
      })
    })

    val items = priceLines.flatMap(matches => {
      val name = matches.group(1).trim
      val prices = Try(priceRegex.findFirstMatchIn(matches.group(3).trim).get.group(2)).getOrElse("0")
      name -> prices
      Try_(InvoiceLine(name, prices.toDouble, 1)).toOption
    })
    val notNullData = items.filter{ l =>
      val name = l.productName.toLowerCase
      name.nonEmpty && !name.contains("total")  && !name.contains("desc")
    }
    val res = Invoice(invoiceId = "NA" ,invoiceDate = "23-6-2017", storeName = "Generic", totalAmount = 0.0, items = notNullData)
    println(res)

    res
  }


  def main(args: Array[String]) {
    val l = Source.fromFile(new File("/Users/hirendra.thakur/Documents/out.txt")).getLines().toList
    parse(l)
  }
}

private object BigBasketParserService extends ParserModel {

  override def parse(lines: List[String]): Invoice = {
    val productLines = lines.filter(l => l.contains("Rs") && !l.toLowerCase.contains("total") && !l.toLowerCase.contains("payable"))
    val invoiceLine = lines.filter(_.contains("Order ID"))
//    println(s"invoiceLine.head ")
//    println(invoiceLine)
    getFormatedResponse(productLines, invoiceLine)
  }

  private def getFormatedResponse(lines: List[String], invoiceLine: List[String]): Invoice = {
    val regex1 = ".+?(?=Rs)(.*)".r
    val regex2 = ".+?(?=Rs)".r
    val regex3 = "([0-9]*[.]?[0-9]+)".r
//    val invoiceRegex = "(.*) (.*)".r
    val invoiceRegex = "Order ID *([A-Z0-9-]*) *".r
    val invoiceNumber = try invoiceRegex.findFirstMatchIn(invoiceLine.head).get.group(2).toString catch {case e:Exception=>"BBO-28454607-150617"}
    val formattedLines = lines.flatMap { line =>
      val group1 = getGroup(regex2, 0, line).toString.trim
      val group2 = getGroup(regex1, 1, line)
      val price = getFirstMatch(regex3, group2)
      val quantity = getSecondMatch(regex3, group2)
      val r1 = "[A-Za-z]".r
      val firstIdx = group1.indexOfSlice(r1.findFirstIn(group1).getOrElse(""))
      var tmpProdName = group1.substring(firstIdx)
      if(tmpProdName.length >= 7 ) {
        if (tmpProdName.head == 'A' || tmpProdName.head == 'M' || tmpProdName.head == 'M')
          tmpProdName = tmpProdName.substring(1)
        if (tmpProdName.head == 'A' || tmpProdName.head == 'M' || tmpProdName.head == 'M')
          tmpProdName = tmpProdName.substring(1)
        if (tmpProdName.length < 7)
          tmpProdName = ""
      }
      Try_(InvoiceLine( productName = tmpProdName, originalPrice = price.toDouble, quantity = quantity.toDouble )).toOption
    }
    val notNullData = formattedLines.filter(_.productName.nonEmpty)
    Invoice(invoiceId = invoiceNumber,invoiceDate = "23-6-2017", storeName = "BigBasket", totalAmount = 0.0, items = notNullData)
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


