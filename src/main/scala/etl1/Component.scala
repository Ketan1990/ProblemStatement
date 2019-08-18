package etl1

trait Component[I, O] extends {
  def operation: I => Either[String, O]
}

