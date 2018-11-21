package java.util.regex

import java.util

/**
  * @note internal use only
  */
object Betrayers {

  implicit class PatternBetrayer(val pattern: Pattern) extends AnyVal {
    def _namedGroups: util.Map[String, Integer] = pattern.namedGroups()
  }

}