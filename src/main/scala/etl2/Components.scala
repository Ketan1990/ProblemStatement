package etl2

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}

import etl1.Output
import scalaz.OptionT

import scala.io.Source
import scala.util.{Failure, Success, Try}

case class Components[I, O](operation: I => Either[String, O])

trait Record

case class SingleLine(data: String) extends Record

case class Pairs(map: Map[String, Int]) extends Record

case class MetaRecords(metaData: String, record: Record)

object Components {


  def compose[OuterInput, InnerInput, Output](
                                               outer: Components[OuterInput, InnerInput],
                                               inner: Components[InnerInput, Output]
                                             ) =
    Components[OuterInput, Output](
      operation = (dataframe) => outer.operation(dataframe).flatMap(inner.operation)
    )

  val capitalization: Components[MetaRecords, MetaRecords] =
    Components[MetaRecords, MetaRecords](
      operation = input => input.record match {
        case SingleLine(a) => Right(MetaRecords(input.metaData, SingleLine(a.toUpperCase)))
        case Pairs(map) => Right(MetaRecords(input.metaData, Pairs(map.map(pair => (pair._1.toUpperCase, pair._2)))))
      }
    )


  val capitalization1: Components[List[MetaRecords], List[MetaRecords]] =
    Components[List[MetaRecords], List[MetaRecords]](
      operation = input => {
        val a: Seq[Either[String, MetaRecords]] = for {
          mr <- input
        } yield capitalization.operation(mr)
       ???
      }:Either[String,List[MetaRecords]]
  )



  def fileList(filePath: String): Either[String, List[File]] = {
    val directory = new File(filePath)
    if (directory.isDirectory) Right(directory.listFiles(_.isFile).toList) else Left("Check File path")
  }

  def extractContent(files: List[File]): List[MetaRecords] =
    files.map(file => MetaRecords(file.getName, SingleLine(Source.fromFile(file).mkString)))


  val dirInput: Components[String, List[MetaRecords]] = Components[String, List[MetaRecords]](
    operation = dir => for {
      list <- fileList(dir)
    } yield extractContent(list)
  )

  def writer(path: String) = new PrintWriter(new File(path))

  def writeSigleLine(writer: PrintWriter, d: String): Either[String, Unit] = {
    val written = Try(writer.write(d))
    writer.close()
    written.map(_ => Right()).getOrElse(Left("Failed to Write"))
  }

  def writesPairs(writer: PrintWriter, value: Map[String, Int]): Either[String, Unit] = {
    val written = Try(value.foreach(pair => writer.write(pair.toString())))
    writer.close()
    written.map(_ => Right()).getOrElse(Left("Failed to Write"))
  }

  def writeInputs(dir: String, metaRecord: MetaRecords): Either[String, Unit] = metaRecord match {
    case MetaRecords(fn, SingleLine(line)) => writeSigleLine(writer(dir.concat("/" + fn)), line)
    case MetaRecords(fn, Pairs(map)) => writesPairs(writer(dir.concat("/" + fn)), map)
  }

  def dirOutput(dir: String): Components[List[MetaRecords], Unit] = Components[List[MetaRecords], Unit](
    operation = inputs => {
      (for {mr <- inputs} yield writeInputs(dir, mr)).head
    }
  )
}

object Apps extends App {

  import Components._

  val destionationPath = "/home/ketan/Documents/Problemdomain/data1"
  val result: Components[String, Unit] = compose(dirInput,dirOutput(destionationPath))

  result match {
    case Left(msg) => println(msg)
    case Right(ls) => println(ls)
  }
}
