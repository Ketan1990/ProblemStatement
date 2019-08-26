package etl2.service
import java.io.File
import etl2.entity._

object Components {
  val fileInputComponent: Component[FileSource, MetaRecords[Line]] = Component[FileSource, MetaRecords[Line]] {
    Input => {
      val file = new File(Input.path)
      for {lines <- FileOpration.readFile(file)}
        yield MetaRecords(file.getName, lines.map(l => Line(l)))
    }
  }


  def toCapitalize:Component[MetaRecords[Line],MetaRecords[Line]] = Component[MetaRecords[Line],MetaRecords[Line]](
    operation = inputs => {
      val lines = inputs.lines.map {
        case Line(a) => Line(a.toUpperCase())
      }
       Right(MetaRecords(inputs.sourceName,lines))
    }
  )

  def wordCount:Component[MetaRecords[Line],MetaRecords[Record]] = Component[MetaRecords[Line],MetaRecords[Record]](
    operation = input => {
      val counts =input.lines.flatMap(_.data.split("\\s+"))
        .map(_.stripSuffix(",").stripSuffix("."))
        .distinct
        .map(word => word.toLowerCase -> 1).groupBy(_._1).view.mapValues(_.length).toList
        .map(pair => KeyValue(pair._1,pair._2))

        Right(MetaRecords(input.sourceName,counts))

    }
  )

  def fileOutputComponent(dir: Directory): Component[MetaRecords[Record], Unit] = Component[MetaRecords[Record], Unit](
    operation = inputs => for {
        fileWriter <- FileOpration.fileWriter(dir.path, inputs.sourceName)
      } yield FileOpration.saveFile(fileWriter,inputs.lines)
  )
}
