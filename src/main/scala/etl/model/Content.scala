package etl.model

sealed trait Content
case class Sentence(metaData:String="", text:String) extends  Content
case class Pairs(metaData:String="",map: Map[String,Int]) extends Content

