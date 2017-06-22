package com.flipkart.sabjiwala.services

import scala.sys.process._

import com.flipkart.sabjiwala.utils.StringUtils

import scala.util.matching.Regex

/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
object ParserService {

  def parse(file:String): List[String] ={
    val outFile = s"/tmp/read_${StringUtils.generateRandomStr(6)}"
    val cmd = s"tesseract $file $outFile -l eng -c preserve_interword_spaces=1"
    val output = cmd.!!
    val source = scala.io.Source.fromFile(s"$outFile.txt")
    val lines = try source.getLines.toList.filter(_.contains("Rs")) finally source.close()
    getFormatedResponse(lines)
  }

  def getFormatedResponse(lines: List[String]): List[String] ={
    val regex1 = ".+?(?=Rs)(.*)".r
    val regex2 = ".+?(?=Rs)".r
    val regex3 = "([0-9]*[.]?[0-9]+)".r
    val formattedLines = lines.map { line =>
      val group1 = getGroup(regex2,0,line).toString.trim
      val group2 = getGroup(regex1,1,line)
      val amount = getFirstMatch(regex3, group2)
      val quantity =  getSecondMatch(regex3,group2)
      var x = List()
      regex3.findAllMatchIn(line).toList
      List(group1, amount, quantity).mkString(",")
    }
    formattedLines
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
