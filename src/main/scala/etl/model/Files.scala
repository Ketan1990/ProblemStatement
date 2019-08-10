package etl.model

import java.io.File

//string => list of files
case class Files(dir:String) {

  def context: Option[List[File]] = {
    val file = new File(dir)
    if(file.exists() && file.isDirectory)
      Some(file.listFiles().filter(_.isFile).toList)
    else
      None
  }
}

