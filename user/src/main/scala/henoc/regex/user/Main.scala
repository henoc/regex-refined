package henoc.regex.user

import java.util.regex.Pattern

import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import henoc.regex.refined.string.{GroupCount, HasGroupName, Matches, NoGroup}
import shapeless.syntax.singleton._

object Main {
  def main(args: Array[String]): Unit = {
    val regex: String Refined HasGroupName[W.`"group"`.T] = "abc(?<group>def)"
    var abc = Pattern.CANON_EQ.narrow
    abc = 5.narrow
    println(regex)
    println(abc)
  }
}
