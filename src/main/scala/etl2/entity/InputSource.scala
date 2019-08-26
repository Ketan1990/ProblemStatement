package etl2.entity

trait Source
case class FileSource(path:String) extends Source
case class DirectorySource(path:String) extends Source





