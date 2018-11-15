package henoc.regex.benchmark

import java.util.regex.Pattern

import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.generic.Equal
import henoc.regex.refined.regex_string._
import shapeless.syntax.singleton._
import henoc.regex.macros._
import henoc.regex.refined._

object Main {

  def main(args: Array[String]): Unit = {
    val str = new stringRegex[W.`"(.).(.)"`.T] {}
    "abc" match {
      case str(a, c) => println(s"a = $a, c = $c")
      case _ => println("no!")
    }
  }

}
