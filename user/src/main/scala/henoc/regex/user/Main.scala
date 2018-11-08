package henoc.regex.user

import java.util.regex.Pattern

import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import henoc.regex.refined.regex_string._
import shapeless.syntax.singleton._

object Main {
  def main(args: Array[String]): Unit = {
    val regex: String Refined JsRegexCompatible = "abc(?:def)f"
    println(regex)
  }
}
