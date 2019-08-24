package etl2.service

import etl2.entity.{DirectoryInput, DirectoryOutputSource, Record}


trait ETL[InputSource,OutputSource,Record] {
  def input(directorySource: InputSource): Either[String, List[Record]]
  def transform(record: List[Record]):Components[List[Record],List[Record]] => Either[String, List[Record]]
  def output(outputSource: OutputSource, records: List[Record]): Either[String, Unit]

  def execute(inputSource: InputSource, outputSource: OutputSource):Components[List[Record],List[Record]] => Either[String, Unit] =
    (com) => for {
    records <- input(inputSource)
    transformedRecords <- transform(records)(com)
    _ <- output(outputSource, transformedRecords)
  } yield()

}
object  ETLService {

  import Components._

 val etlDirectorySource = new ETL[DirectoryInput,DirectoryOutputSource,Record] {
   override def input(directorySource: DirectoryInput): Either[String, List[Record]] =
     dirInputComponent.operation(directorySource)

   override def transform(record: List[Record]):Components[List[Record],List[Record]] => Either[String, List[Record]] =
     (component) => component.operation(record)

   override def output(outputSource: DirectoryOutputSource, records: List[Record]): Either[String, Unit] =
     dirOutputComponent(outputSource).operation(records).map(_.head)
 }
}
