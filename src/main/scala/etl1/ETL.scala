package etl1


trait ETL {
  def input:Input => Either[String,(List[MetaRecord])]
  def operation:List[Component[Record,Record]]
  def output:Output => Either[String,Unit]
  def foldOperation(records:List[Record]) = ???
}

object ETLFile {
  def etl = new ETL {
    override def input: Input => Either[String, List[MetaRecord]] = ???
    override def operation: List[Component[Record, Record]] = ???
    override def output: Output => Either[String, Unit] = ???
  }


}