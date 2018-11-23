package henoc.regex.refined

import org.scalatest.FunSuite
import regex_string._
import eu.timepit.refined.api._
import eu.timepit.refined.W
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.Regex
import eu.timepit.refined.auto._

class RegexStringTest extends FunSuite {
  test("GroupCount should work") {
    assertCompiles(
      "val a: String Refined GroupCount[Equal[W.`0`.T]] = \"abc\""
    )
    assertCompiles(
      "val a: String Refined GroupCount[Equal[W.`1`.T]] = \"a(b)c\""
    )
    assertCompiles(
      "val a: String Refined GroupCount[Less[W.`3`.T]] = \"a(b)(c)\""
    )
    assertDoesNotCompile(
      "val a: String Refined GroupCount[Equal[W.`0`.T]] = \"a(b)c\""
    )
    assertDoesNotCompile(
      "val a: String Refined GroupCount[Equal[W.`1`.T]] = \"abc\""
    )
    assertDoesNotCompile(
      "val a: String Refined GroupCount[Less[W.`3`.T]] = \"(a)(b)(c)\""
    )
  }

  test("Matches should work") {
    assertCompiles(
      "val a: String Refined Matches[W.`\"123\"`.T] = \"^[1-9]+[0-9]*$\""
    )
    assertDoesNotCompile(
      "val a: String Refined Matches[W.`\"12.345\"`.T] = \"^[1-9]+[0-9]*$\""
    )
  }

  test("HasGroupName should work") {
    assertCompiles(
      "val a: String Refined HasGroupName[W.`\"foo\"`.T] = \"(?<foo>bar)|baz\" "
    )
    assertDoesNotCompile(
      "val a: String Refined HasGroupName[W.`\"foo\"`.T] = \"(?<bar>baz)|foo\" "
    )
  }

  test("MatchFlags should work") {
    assertCompiles(
      "val a: String Refined MatchFlags[W.`\"ix\"`.T] = \"(?idux)abc\" "
    )
    assertDoesNotCompile(
      "val a: String Refined MatchFlags[W.`\"ix\"`.T] = \"(?duxm)abc\" "
    )
  }

  test("JsRegex should work") {
    assertCompiles(
      """val a: String Refined JsRegex = "abx[\\b]cde" """
    )
    assertCompiles(
      """val a: String Refined Regex = "a++bc" """
    )
    assertDoesNotCompile(
      """val a: String Refined Regex = "abx[\\b]cde" """
    )
    assertDoesNotCompile(
      """val a: String Refined JsRegex = "a++bc" """
    )
  }

  test("FullMatchPattern should work") {
    assertCompiles(
      """val a: String Refined FullMatchPattern = "^abc$" """
    )
    assertDoesNotCompile(
      """val a: String Refined FullMatchPattern = "^abc" """
    )
    assertDoesNotCompile(
      """val a: String Refined FullMatchPattern = "abc$" """
    )
  }
}
