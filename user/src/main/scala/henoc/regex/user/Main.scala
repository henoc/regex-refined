package henoc.regex.user

import java.util.regex.Pattern

import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import henoc.regex.refined.string._
import shapeless.syntax.singleton._

object Main {
  def main(args: Array[String]): Unit = {
    val regex: String Refined MatchFlags[W.`"_"`.T] = "(?i)abc(?<group>def)"
    println(regex)
  }
}
