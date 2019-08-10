package etl.service.component

import java.io.File

import etl.model.{Content, Files, Sentence}
import etl.service.InputSource

import scala.io.{BufferedSource, Source}

trait FileInputSource extends InputSource[Files, Content] {

  private def read(file: File) = {
    val source: BufferedSource = Source.fromFile(file)
    val content = source.mkString
    (content, source.close())
  }


  override def extractContent(files: Files): List[Content] = files.context match {
    case None => List.empty[Content]
    case Some(fils) => fils.map(file => Sentence(file.getName, read(file)._1))
  }
}

object FileInputSource extends FileInputSource

