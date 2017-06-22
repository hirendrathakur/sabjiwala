package com.flipkart.sabjiwala.services

import scala.sys.process._

import com.flipkart.sabjiwala.utils.StringUtils

import scala.util.matching.Regex

/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
object ParserService {

  def parse(file:String): List[Map[String, Any]] ={
    val outFile = s"/tmp/read_${StringUtils.generateRandomStr(6)}"
    val cmd = s"tesseract $file $outFile -l eng -c preserve_interword_spaces=1"
    val output = cmd.!!
    val source = scala.io.Source.fromFile(s"$outFile.txt")
    var lines :List[String] = List()
    var productLines :List[String] = List()
    var invoiceLine :List[String] = List()
    try {
      lines = source.getLines.toList
//      println("lines")
//      println(lines)
      productLines = lines.filter(_.contains("Rs"))

      invoiceLine = lines.filter(_.contains("Invoice"))
//      println("invoice lines")
//      println(invoiceLine)

    }
    finally source.close()
    getFormatedResponse(productLines)
  }

  def getFormatedResponse(lines: List[String]): List[Map[String, Any]] ={
    val regex1 = ".+?(?=Rs)(.*)".r
    val regex2 = ".+?(?=Rs)".r
    val regex3 = "([0-9]*[.]?[0-9]+)".r
    val formattedLines = lines.map { line =>
      val group1 = getGroup(regex2,0,line).toString.trim
      val group2 = getGroup(regex1,1,line)
      val price = getFirstMatch(regex3, group2)
      val quantity =  getSecondMatch(regex3,group2)
//      List(group1, amount, quantity).mkString(",")
      val r1 = "[A-Za-z]".r
      val firstIdx = group1.indexOfSlice(r1.findFirstIn(group1).getOrElse(""))
      Map("name" -> group1.substring(firstIdx), "price" -> price, "quantity" -> quantity)
    }
    formattedLines.filter(_("name").nonEmpty)
  }

  def getGroup(regex:Regex, key:Int, line: String):String ={
    try{
        regex.findFirstMatchIn(line).get.group(key)
    } catch {
      case e:Exception=>""
    }
  }

  def getFirstMatch(regex:Regex, line: String):String ={
    try{
      val res = regex.findAllMatchIn(line).toList
      res(0).toString()
    } catch {
      case e:Exception=>""
    }
  }

  def getSecondMatch(regex:Regex, line: String):String ={
    try{
      val res = regex.findAllMatchIn(line).toList
      res(1).toString()
    } catch {
      case e:Exception=>""
    }
  }
}
