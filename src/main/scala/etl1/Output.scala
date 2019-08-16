package etl1

trait Output
case class DirOutput(destinatioPath:String,records:List[MetaRecord]) extends Output
