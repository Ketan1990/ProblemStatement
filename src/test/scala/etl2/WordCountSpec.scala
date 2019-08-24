package etl2

import etl2.service.WordCounter
import org.scalatest.{BeforeAndAfter, FlatSpec}

import scala.collection.immutable.HashMap

class WordCountSpec extends UnitSpec with BeforeAndAfter {

  "A WordCounter" should "Insert Word Or IncrementCount" in {
    //given
    val dummy = HashMap[String, Int]("hello" -> 1, "world" -> 1)
    val expected = HashMap[String, Int]("hello" -> 2, "world" -> 1, "this" -> 1)
    //when
    val wordCount = WordCounter.insert("Hello", dummy)
    val actual = WordCounter.insert("this", wordCount)
    //then
    assert(actual == expected)
  }

  it should "insert word in a bulk and incrementCounter" in {
    //given
    val dummy = List("Hello","hello" ,"world" ,"this")
    val expected = HashMap[String, Int]("hello" -> 2, "world" -> 1, "this" -> 1)
    //when
    val actual  =  WordCounter.bulkInsert(dummy)
    //then
    assertResult(expected)(actual.right.value)
  }
}


