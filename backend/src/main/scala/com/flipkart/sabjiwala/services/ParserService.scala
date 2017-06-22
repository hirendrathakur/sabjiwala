package com.flipkart.sabjiwala.services

import java.io.File
import scala.sys.process._

import com.flipkart.sabjiwala.utils.StringUtils

/**
  * Created by kinshuk.bairagi on 22/06/17.
  */
object ParserService {

  def parse(file:String): List[String] ={
    val outFile = s"/tmp/read_${StringUtils.generateRandomStr(6)}.txt"
    val cmd = s"tesseract $file $outFile -l eng -c preserve_interword_spaces=1"
    val output = cmd.!!
    val source = scala.io.Source.fromFile(outFile)
    val lines = try source.getLines.toList.filter(_.contains("Rs")) finally source.close()

    lines
  }
}
