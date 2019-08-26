package etl2

import etl2.entity.{KeyValue, Line, MetaRecords, Record}
import etl2.service.{Component, Components}


class ComponentSpec extends UnitSpec {
  "A Capitalize" should " capitalize the text inside record " in {
    //given
    val given = MetaRecords("source",List(Line("Make me Capitalized"),Line("Make me Capitalized")))
    val expected =  MetaRecords("source",List(Line("MAKE ME CAPITALIZED"),Line("MAKE ME CAPITALIZED")))

    //when
    val actual = Components.toCapitalize.operation(given)

    //then
    assertResult(expected)(actual.right.value)
  }

  "A Components" should " count all the words " in {
    //given
    val given = MetaRecords("source",List(Line("Make me Capitalized."),Line("Make Me, capitalized")))
    val expected =  MetaRecords("source",List(KeyValue("me",2), KeyValue("make",1), KeyValue("capitalized",2)))


    //when
    val actual = Components.wordCount.operation(given)

    //then
    assertResult(expected)(actual.right.value)
  }
}
