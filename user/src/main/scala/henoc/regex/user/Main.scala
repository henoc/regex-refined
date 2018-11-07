package henoc.regex.user

import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import henoc.regex.refined.string.{GroupCount, HasGroupName, Matches, NoGroup}

object Main {
  def main(args: Array[String]): Unit = {
    val regex: String Refined HasGroupName[W.`"group"`.T] = "abc(?<group>def)"
    println(regex)
  }
}
