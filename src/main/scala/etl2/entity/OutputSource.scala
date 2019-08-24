package etl2.entity

trait OutputSource
case  class FileOutputSource(destinationPath:String)
case class DirectoryOutputSource(path:String)
