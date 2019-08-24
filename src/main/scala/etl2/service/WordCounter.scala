package etl2.service

import scala.annotation.tailrec
import scala.collection.immutable.HashMap

object WordCounter {
  def insert[A](word: String, counterMap: HashMap[String, Int]) = {
    val key = word.toLowerCase
    if (counterMap.contains(key))
      counterMap.+((key, counterMap(key) + 1))
    else
      counterMap.+((key, 1))
  }

  def bulkInsert(words: List[String]) = {
    @tailrec
    def loop(words: List[String], wordCounter: HashMap[String, Int]): HashMap[String, Int] = words match {
      case Nil => wordCounter
      case x :: xs => loop(xs, insert(x, wordCounter))
    }
    if(words.isEmpty) Left("Empty List") else Right(loop(words, HashMap.empty[String, Int]))
  }

  def bulkInsert1(words: List[String]) = {
    words.foldLeft(HashMap.empty[String, Int]){(counter,word) => insert(word,counter)}
  }
}
