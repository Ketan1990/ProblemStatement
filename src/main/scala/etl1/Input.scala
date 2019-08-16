package etl1

trait Input{
  def sorce:String
}
case class Directory(sorce:String) extends Input

