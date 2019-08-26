package etl2

import etl2.entity.{Directory, FileSource}
import etl2.service.Component._
import etl2.service.Components._


object Application {

  def main(args: Array[String]): Unit = {
    val input = FileSource("/home/ketan/Documents/Problemdomain/data/test")
    val output = Directory("/home/ketan/Documents/Problemdomain/data1")

    val flow = fileInputComponent -> toCapitalize -> wordCount  -> fileOutputComponent(output)
    flow.operation(input)

  }
}
