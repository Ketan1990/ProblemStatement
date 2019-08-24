package etl2.service

import scala.annotation.tailrec

object Traverse {


  def sequence[Record](list:  List[Either[String, Record]]): Either[String, List[Record]] = {

    @tailrec
    def loop(result: List[Either[String, Record]], value: Either[String, List[Record]]):
    Either[String, List[Record]] = result match {
      case Nil => value
      case h::ls =>  loop(ls,for (xs <- value; x <- h) yield  xs.appended(x) )
    }
    loop(list, Right(Nil))
  }
}
