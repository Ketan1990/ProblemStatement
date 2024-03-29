package etl.service.component

import java.io.{File, FileOutputStream, PrintWriter}

import etl.model.{Content, Pairs, Sentence}
import ContentWriter._

object FileOutputSource {


  def load(dirPath: String, content: Content): Unit = content match {
    case Sentence(m, data) => write(buildPath(dirPath,m), data)
    case Pairs(m, datapairs) => write(buildPath(dirPath,m), datapairs)
  }

  def buildPath(dir:String,filename:String): String = dir.concat("/" + filename)

  def pairFormat(pair: (String, Int)): String = pair.toString() + "\n"

  def write[Data:ContentWriter](path: String, data: Data): Unit =
    implicitly[ContentWriter[Data]].write(new PrintWriter(new File(path)), data).close()

}
