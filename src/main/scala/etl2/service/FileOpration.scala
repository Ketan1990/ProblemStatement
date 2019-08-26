package etl2.service

import java.io.{BufferedWriter, File}
import java.nio.file.{Files, Paths}

import etl2.entity.{KeyValue, Line, MetaRecords, Record}

import scala.collection.LinearSeq
import scala.io.Source
import scala.util.{Failure, Success, Try}

object FileOpration {
  def saveFile(fileWriter: BufferedWriter, inputs: List[Record]): Unit =  {
    inputs.foreach {
      case KeyValue(k,v) => fileWriter.write((k , v).toString()) ; fileWriter.newLine()
      case Line(data) => fileWriter.write(data.toString) ; fileWriter.newLine()
    }
    fileWriter.close()
  }

  def fileWriter(dirPath:String, fileName:String): Either[String, BufferedWriter] = {
    val filePath = dirPath.concat(File.separator + fileName)
    Try(Files.newBufferedWriter(Paths.get(filePath))) match {
      case Failure(msg) => Left("File not found ")
      case Success(v) => Right(v)
    }
  }

  def readFile(file:File) ={
    Try(Source.fromFile(file)) match {
      case Failure(_) => Left("File is not able to read")
      case Success(value) => Right(value.getLines().toList)
    }
  }
}
