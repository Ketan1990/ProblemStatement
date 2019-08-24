package etl


import etl.model.{Content, Files}
import etl.service.component.{Capitalization, FileInputSource, FileOutputSource, WordCounter}

object ETLFileService {

  def input(files: Files) = FileInputSource.extractContent(files)


  def transformation(flist: Content) = Capitalization(flist)


  def output(outputSource: String, content: Content) = FileOutputSource.load(outputSource, content)

  def execute(inputSource:Files,outputFileSource: String) =
    (for {
      flist <- input(inputSource)
      x <- transformation(flist)
    } yield output(outputFileSource,x)).headOption
}
