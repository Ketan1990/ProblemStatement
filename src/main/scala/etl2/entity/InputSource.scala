package etl2.entity

trait InputSource
case class FileInput(path:String) extends InputSource
case class DirectoryInput(path:String) extends InputSource





