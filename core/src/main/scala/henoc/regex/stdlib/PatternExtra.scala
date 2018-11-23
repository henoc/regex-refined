package henoc.regex.stdlib

import java.util
import java.util.regex.{PatternSyntaxException => PatternExceptionOriginal}

import scala.util.Try

private[regex] class PatternExtra(val pattern: Pattern) extends AnyVal {

  def namedGroups(): util.Map[String, Integer] = pattern.namedGroups()

  def groupCount(): Int = pattern.matcher("").groupCount()

  def flags(): Int = pattern.flags()

  def matches(text: String): Boolean = pattern.matcher(text).matches()

}
private[regex] object PatternExtra {

  def compile(regex: String): PatternExtra =
    Try(new PatternExtra(Pattern.compile(regex))).recover {
      case e: PatternSyntaxException => throw new PatternExceptionOriginal(e.getDescription, e.getPattern, e.getIndex)
    }.get

}
