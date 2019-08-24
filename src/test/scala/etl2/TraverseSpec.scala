package etl2

import etl2.service.Traverse

class TraverseSpec extends UnitSpec {
  "A Traverse" should "sequencing list of number with RightBiased" in {
    //given
    val given  = List(Right(2),Right(4),Right(6),Right(8))
    //when
    val actual = Traverse.sequence(given)
    val expected = Right(List(2,4,6,8))
    //then
    assertResult(expected)(actual)
  }

  "A Traverse" should "sequencing list of number with Left" in {
    //given
    val given  = List(Right(2),Right(4),Right(6),Right(8),Left("Not even Number sequence"))
    //when
    val actual = Traverse.sequence(given)
    val expected =  Left("Not even Number sequence")
    //then
    assertResult(expected)(actual)
  }
}
