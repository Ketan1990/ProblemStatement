package etl1

trait Record

case class SingleLine(data: String) extends Record

case class Pairs(map: Map[String,Int]) extends Record

case class MetaRecord(meta: String, record: Record)