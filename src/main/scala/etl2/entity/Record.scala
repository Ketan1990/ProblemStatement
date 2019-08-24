package etl2.entity

import java.io.{File, FileWriter}

import etl2.entity.Record.FileWrite.{fileWriter, writeRecord, writeSingleLine, writesPairs}
import etl2.service.Traverse

import scala.io.Source
import scala.util.{Failure, Success, Try}

trait Record {
  def sourceName: String
}

case class Content(sourceName: String, data: String) extends Record

case class Pairs(sourceName: String, map: Map[String, Int]) extends Record


object Record {

  import FileWrite._

  def readSingleFile(file: File): Either[String, Content] = {
    Try(Source.fromFile(file).mkString) match {
      case Failure(msg) => Left("File Not Found")
      case Success(value) => Right(Content(file.getName, value))
    }
  }

  def readFromFiles(files: List[File]) = Traverse.sequence(files.map(f => readSingleFile(f)))

  def writeIntoFile(dir: String, records: Record): Either[String, Unit] = records match {
    case Content(fs, line) => writeRecord(fileWriter(dir, fs), line)(writeSingleLine)
    case Pairs(fs, map) => writeRecord(fileWriter(dir, fs), map)(writesPairs)
  }


  object FileWrite {

    def fileWriter(dir: String, fileName: String) = {
      val fullPath = dir + File.separator + fileName
      new FileWriter(new File(fullPath))
    }

    def writeSingleLine(writer: FileWriter, value: String): Unit = {
      writer.write(value)
      writer.close()
    }

    def writesPairs(writer: FileWriter, value: Map[String, Int]): Unit = {
      value.foreach(pair => writer.write(pair.toString()))
      writer.close()
    }

    def writeRecord[Writer, A](writer: Writer, a: A)(func: (Writer, A) => Unit): Either[String, Unit] = {
      Try(func(writer, a)).map(_ => Right()).getOrElse(Left("Failed to Write"))
    }
  }

}