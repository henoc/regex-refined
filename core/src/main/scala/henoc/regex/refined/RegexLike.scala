package henoc.regex.refined

import java.util.regex.Pattern

import eu.timepit.refined.api._
import eu.timepit.refined.api.Validate
import eu.timepit.refined.generic.Equal
import shapeless.Nat._0

import scala.util.matching.Regex

object RegexLike {

  /**
    * Predicate that checks if the group count of a regex-like value satisfies P.
    */
  final case class GroupCount[P](p: P)

  /**
    * Predicate that checks if a regex-like value has no capturing groups.
    */
  type NoGroup = GroupCount[Equal[_0]]

  object GroupCount {

    implicit def groupCountValidate[Target: RegexLike, Predicate, RP](implicit vint: Validate.Aux[Int, Predicate, RP]):
      Validate.Aux[Target, GroupCount[Predicate], GroupCount[vint.Res]] = {

      def provide[T](fn: T => Int) = new Validate[T, GroupCount[Predicate]] {
        override type R = GroupCount[vint.Res]

        override def validate(t: T): Res = {
          val r = vint.validate(fn(t))
          Result.fromBoolean(r.isPassed, GroupCount(r))
        }

        override def showExpr(t: T): String =
          s"RegexGroupCount(${vint.showExpr(fn(t))})"
      }

      provide[Target](target => groupCount(implicitly[RegexLike[Target]].pattern(target)))
    }

  }

  lazy val compile: String => Pattern = memoize(Pattern.compile)

  lazy val groupCount: Pattern => Int = memoize(pattern => {
    pattern.matcher("").groupCount()
  })

  implicit val stringRegexLike: RegexLike[String] = (t: String) => compile(t)
  implicit val regexRegexLike: RegexLike[Regex] = (t: Regex) => t.pattern
  implicit val patternRegexLike: RegexLike[Pattern] = (t: Pattern) => t

}

trait RegexLike[T] {

  def pattern(t: T): Pattern

}