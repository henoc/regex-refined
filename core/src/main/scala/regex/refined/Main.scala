package regex.refined

import regex.macros._

object Main {
  def main(args: Array[String]): Unit = {
    println(groupCount("ab(c)d(e)"))
    println(groupCount("a(b)"))
  }
}
