package etl2

import etl2.entity.{Content, Pairs, Record}
import etl2.service.Components

import scala.collection.immutable.HashMap

class ComponentSpec extends UnitSpec {
  "A Capitalize" should " capitalize the text inside record " in {
    //given
    val given = Content("source","Make me Capitalized")
    val expected =  Content("source","MAKE ME CAPITALIZED")

    //when
    val actual = Components.capitalize.operation(given)

    //then
    assert(actual.right.value == expected)
  }

  it should " return message on Empty Record " in {
    //given
    val given =  Content("source","")
    val expected = "Record is empty"

    //when
    val actual = Components.capitalize.operation(given)

    //then
    assert(actual.left.value === expected)
  }


  "A wordCalculate" should " count words from sentence " in {
    //given
    val given = Content("source","Hello hello world, this")
    val expected =  Pairs("source",HashMap("hello" -> 2, "world" -> 1, "this" -> 1))

    //when
    val actual: Either[String, Record] = Components.wordCalculate.operation(given)

    //then
    assertResult(expected)(actual.right.value)
  }


  "A wordsCounter" should "Extract contain from list files" in {
    //given
    val given = List(Content("source","Hello hello world,"),Content("source","Hello hello world, t"))
    val expected = List(entity.Pairs("source",HashMap("hello" -> 2, "world" -> 1)), entity.Pairs("source",HashMap("hello" -> 2, "world" -> 1, "t" -> 1)))

    //when
    val actual:Either[String,List[Record]] = Components.WordCalculateRecords.operation(given)

    //then
    assertResult(expected)(actual.right.value)

  }
}
