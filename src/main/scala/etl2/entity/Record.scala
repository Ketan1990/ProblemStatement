package etl2.entity


trait Record
case class Line(data: String) extends Record
case class KeyValue(k:String,v:Int) extends Record

case class MetaRecords[Record](sourceName:String,lines:List[Record])