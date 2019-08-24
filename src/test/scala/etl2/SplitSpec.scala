package etl2

import etl2.service.WordsSplit

class SplitSpec extends UnitSpec {
  "A Split" should "crete chunks of words " in {
    //given
    val given = "Hello ,,world,,,,,, hello"
    val expected = Array("Hello","world","hello")
    //when
    val actual = WordsSplit(given)

    //then
    assertResult(expected)(actual.right.value)
  }


}
