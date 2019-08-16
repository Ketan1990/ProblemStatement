package etl1

trait Component[I,O] {
 def operation: I => Either[String,O]
}
