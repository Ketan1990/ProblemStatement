package etl1

import java.io.{File, PrintWriter, Writer}

import scala.collection.immutable.TreeMap
import scala.io.Source
import scala.util.{Failure, Success, Try}

trait CoreComponents {

  def dirInputSource: Component[Directory, List[MetaRecord]] = new Component[Directory, List[MetaRecord]] {

    def fileList(filePath: String): Either[String, List[File]] = {
      val directory = new File(filePath)
      if (directory.isDirectory) Right(directory.listFiles(_.isFile).toList) else Left("Check File path")
    }

    def extractContent(files: List[File]): List[MetaRecord] =
      files.map(file => MetaRecord(file.getName, SingleLine(Source.fromFile(file).mkString)))

    override def operation: Directory => Either[String, List[MetaRecord]] = { dir =>
      for {
        list <- fileList(dir.sorce)
      } yield extractContent(list)
    }
  }

  def capitalize = new Component[MetaRecord, MetaRecord] {

    def toUpperCase(record: Record) = record match {
      case SingleLine(a) => SingleLine(a.toUpperCase)
    }

    override def operation: MetaRecord => Either[String, MetaRecord] = mr => Right {
      MetaRecord(mr.meta, toUpperCase(mr.record))
    }
  }


  def wordCounter = new Component[MetaRecord, MetaRecord] {

    def uniqueWords(data: String) = data.split("\\s+").toSet.toList

    val CaseInsensitiveMap: TreeMap[String, Int] = TreeMap.empty[String, Int](Ordering.comparatorToOrdering(String.CASE_INSENSITIVE_ORDER))

    def countes(data: String): Map[String, Int] = {
      val words = uniqueWords(data)
      words.foldLeft(CaseInsensitiveMap) { (map, word) =>
        val newValue = map.getOrElse(word, 0) + 1
        map.+(word -> newValue)
      }
    }

    def wordCounts(record: Record): Record = record match {
      case SingleLine(data) => Pairs(countes(data))
    }

    override def operation: MetaRecord => Either[String, MetaRecord] = mr => Right {
      MetaRecord(mr.meta, wordCounts(mr.record))
    }
  }

  def dirOutPutSource = new Component[DirOutput, Unit] {

    def writer(path: String) = new PrintWriter(new File(path))

    def writeSigleLine(writer: PrintWriter, d: String):Either[String,Unit] = Try(writer.write(d)) match {
      case Failure(msg) => Left("Failed to write")
      case Success(()) => Right(())
    }

    def writesPairs(writer: PrintWriter, value: Map[String, Int]):Either[String,Unit] = Try(value.foreach(pair => writer.write(pair.toString())))
    match {
      case Failure(msg) => Left("Failed to write")
      case Success(()) => Right(())
    }

    def writeInputs(dir: String, metaRecord: MetaRecord):Either[String,Unit] = metaRecord match {
      case MetaRecord(fn, SingleLine(line)) => writeSigleLine(writer(dir + "/" + fn), line)
      case MetaRecord(fn, Pairs(map)) => writesPairs(writer(dir + "/" + fn), map)
    }

    override def operation: DirOutput => Either[String, Unit] = dir => {
      val result: List[Either[String, Unit]] = for {
       mr <- dir.records
      }yield writeInputs(dir.destinatioPath,mr)

      result.head
    }
  }

}


object CoreComponents extends CoreComponents