package etl2

import org.scalatest.{EitherValues, FlatSpec, Matchers, OptionValues}

abstract class UnitSpec extends FlatSpec with Matchers with OptionValues with EitherValues
