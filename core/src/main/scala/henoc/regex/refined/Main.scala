package henoc.regex.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined._
import eu.timepit.refined.generic.Equal
import henoc.regex.refined.string.GroupCount
import eu.timepit.refined.auto._

object Main {
  def main(args: Array[String]): Unit = {
    val regex: String Refined GroupCount[Equal[W.`3`.T]] = "a(b)(c)(d)e"
    println(regex)
  }
}
