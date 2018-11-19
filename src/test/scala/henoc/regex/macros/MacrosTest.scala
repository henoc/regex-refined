package henoc.regex.macros

import org.scalatest.FunSuite

class MacrosTest extends FunSuite {
  test("RegexContext should work") {
    "2018-11-19" match {
      case r"""(\d+$year)-(\d+$month)-(\d+$day)""" =>
        assert(year == "2018")
        assert(month == "11")
        assert(day == "19")
      case _ =>
        fail("fail to pattern matching")
    }
  }
}
