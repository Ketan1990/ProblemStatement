package etl.service.component

import java.io.Writer

import etl.model.Sentence

import scala.util.Try

trait ContentWriter[T] {
  def write(writer: Writer, t: T): Writer
}

object ContentWriter {
  implicit val sentenceWrite: ContentWriter[String] = new ContentWriter[String] {
    override def write(writer: Writer, t: String): Writer = {
      writer.write(t)
      writer
    }
  }


  implicit val PairsWrite: ContentWriter[Map[String, Int]] = new ContentWriter[Map[String, Int]] {
    override def write(writer: Writer, t: Map[String, Int]): Writer = {
      t.foreach(pair => writer.write(pair.toString().concat("\n")))
      writer
    }
  }
}