package henoc.regex.stdlib

import org.scalatest.FunSuite

class PatternExtraTest extends FunSuite {
  test("isFullMatchPattern should work") {
    assert(PatternExtra.compile("^abc$").isFullMatchPattern)
    assert(!PatternExtra.compile("^abc").isFullMatchPattern)
    assert(!PatternExtra.compile("abc$").isFullMatchPattern)
  }
}
