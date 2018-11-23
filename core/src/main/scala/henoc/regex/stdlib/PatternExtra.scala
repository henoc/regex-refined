package henoc.regex.stdlib

import java.util

private[regex] object Implicits {

  implicit class PatternAccessor(val pattern: Pattern) extends AnyVal {

    def namedGroups(): util.Map[String, Integer] = pattern.namedGroups()

  }

}
