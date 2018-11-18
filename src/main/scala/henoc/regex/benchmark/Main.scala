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
    "2018-11-18" match {
      case regex"""(\d+$year)-(\d+$month)-(\d+$day)""" => println(s"year = $year, month = $month, day = $day")
      case _ => println("no!")
    }
  }

}
