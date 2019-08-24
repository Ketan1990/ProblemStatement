package etl

import etl.model.Files
import etl.service.component.{Capitalization, FileInputSource, FileOutputSource, WordCounter}

object Application extends App {
  val a = Files("/home/ketan/Documents/Problemdomain/data")
  val d = "/home/ketan/Documents/Problemdomain/data1"

  ETLFileService.execute(a,d).get

}
