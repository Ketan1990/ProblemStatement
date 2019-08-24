package etl2.service

import java.io.File

import etl2.entity._


case class Components[I, O](operation: I => Either[String, O])


object Components {


  def compose[OuterInput, InnerInput, Output](
                                               outer: Components[OuterInput, InnerInput],
                                               inner: Components[InnerInput, Output]
                                             ): Components[OuterInput, Output] =
    Components[OuterInput, Output](operation = dataframe => outer.operation(dataframe).flatMap(inner.operation))


  val capitalize: Components[Record, Record] =
    Components[Record, Record](
      operation = {
        case Content(f, a) => if (a.isEmpty) Left("Record is empty") else Right(Content(f, a.toUpperCase))
        case Pairs(f, map) => Right(Pairs(f, map.map(pair => (pair._1.toUpperCase, pair._2))))
      }
    )


  val FileInputComponent: Components[FileInput, Record] = Components[FileInput, Record](
    operation = file => Record.readSingleFile(new File(file.path))
  )

  def fileOutputComponent(filePath: FileOutputSource): Components[Record, Unit] = Components[Record, Unit](
    input => Record.writeIntoFile(filePath.destinationPath, input)
  )

  val wordCalculate: Components[Record, Record] = Components[Record, Record](
    operation = {
      case Content(f, data) => for {
        words <- WordsSplit(data)
        wordCount <- WordCounter.bulkInsert(words.toSet.toList)
      } yield Pairs(f, wordCount)

      case p@Pairs(f, data) => ???

    }
  )


  val capitalizeRecords: Components[List[Record], List[Record]] =
    Components[List[Record], List[Record]](
      operation = input => {
        val result = input.map(a => capitalize.operation(a))
        Traverse.sequence(result)
      }
    )


  val WordCalculateRecords: Components[List[Record], List[Record]] = Components[List[Record], List[Record]](
    operation = records => {
      Traverse.sequence(records.map(wordCalculate.operation))
    }

  )

  val dirInputComponent: Components[DirectoryInput, List[Record]] = Components[DirectoryInput, List[Record]](
    operation = dir => for {
      files <- FileOperation.directoryWalk(dir.path)
      records <- Record.readFromFiles(files)
    } yield records
  )


  def dirOutputComponent(dir: DirectoryOutputSource): Components[List[Record], List[Unit]] = Components[List[Record], List[Unit]](
    operation = records => Traverse.sequence(records.map(record => Record.writeIntoFile(dir.path, record)))
  )
}


