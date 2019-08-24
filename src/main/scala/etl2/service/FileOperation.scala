package etl2.service

import java.io.File

import etl2.entity.FileOutputSource

object FileOperation {

  def directoryWalk(filePath: String): Either[String, List[File]] = {
    val directory = new File(filePath)
    if (directory.isDirectory) Right(directory.listFiles(_.isFile).toList) else Left("invalid File path")
  }

  def fileLookUpTable(dirPath: String): Either[String, List[String]] = {
    for {
      files <- directoryWalk(dirPath)
    } yield files.map(file => file.getName)
  }

  def copyFileNames(sourceDirectory: String, destination: String): Either[String, List[FileOutputSource]] = {
    for (fileNames <- fileLookUpTable(sourceDirectory)) yield fileNames
      .map(name => FileOutputSource(destination.concat(File.separator + name)))
  }
}
