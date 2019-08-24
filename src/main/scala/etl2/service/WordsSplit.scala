package etl2.service

object WordsSplit {
  def apply(line:String): Either[String,Array[String]] =
    if(line.isEmpty) Left("input is empty for split") else Right(line.split("(,|\\s)+"))
}
