package etl.service.component
import etl.model.{Content, Pairs, Sentence}
import scala.collection.immutable.TreeMap

object WordCounter {
  def apply(content: Sentence):Option[Content] = content match {
    case Sentence(m,s) => for{
      words <- uniqueWords(s)
    }yield Pairs(m,counter(words))
  }

  def counter(data: List[String]): Map[String, Int] = {
    data.foldLeft(TreeMap.empty[String, Int](_.compareToIgnoreCase(_))) {
      (ls, word) => ls.+(word -> (ls.getOrElse(word, 0) + 1))
    }
  }

  def toSplit(sentence: String): Array[String] = {
    sentence.split("\\s+")
  }

  def uniqueWords(sentence: String): Option[List[String]] = {
    Option(toSplit(sentence).toSet.toList)
  }

}


object Apps extends App {
  val a = Sentence("text","Hello world,  hello ")
  println(WordCounter(a))
}
