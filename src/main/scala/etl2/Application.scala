package etl2

import etl2.entity.{DirectoryInput, DirectoryOutputSource}
import etl2.service.{Components, ETLService}

object Application {
  import Components._
  import ETLService._
  def main(args: Array[String]): Unit = {

    val i = DirectoryInput("/home/ketan/Documents/Problemdomain/data")
    val o = DirectoryOutputSource("/home/ketan/Documents/Problemdomain/data1")
    etlDirectorySource.execute(i,o)(Components.compose(WordCalculateRecords,capitalizeRecords))
  }
}
