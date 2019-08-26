package etl2.entity

trait Output
case  class File(destinationPath:String)
case class Directory(path:String)
