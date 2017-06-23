package com.flipkart.sabjiwala.services

import java.io.File

import com.flipkart.sabjiwala.utils.StringUtils

import scala.sys.process._
import com.flipkart.sabjiwala.utils.StringUtils


/**
  * Created by kinshuk.bairagi on 23/06/17.
  */
object ImageProcessor {

  private val username = System.getProperty("user.name")
  private val baseDir = s"/Users/$username/.sabjiwala/"
  private val makeBaseDir = s"mkdir -p $baseDir".!!

  def apply(file:String):List[String] = {
    val fileId = StringUtils.generateRandomStr(6)
    val cleanFile = s"$baseDir/clean_$fileId.jpg"
    val cleanCmd = s"/Users/$username/Downloads/textcleaner -g -e stretch -f 25 -o 5 -s 1 $file $cleanFile"
    val textCleanOutput = cleanCmd.!!

    val outFile = s"/tmp/read_$fileId"
    val tessCmd = s"tesseract $cleanFile $outFile -l eng -c preserve_interword_spaces=1"
    val OCROutput = tessCmd.!!

    val source = scala.io.Source.fromFile(s"$outFile.txt")
    val lines = try source.getLines().toList finally source.close()
    lines
  }


}
