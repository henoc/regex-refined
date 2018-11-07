package henoc.regex.refined

import java.util.regex.Pattern

import eu.timepit.refined.api._
import eu.timepit.refined.api.Validate
import eu.timepit.refined.generic.Equal
import shapeless.Nat.{_0, _1}
import shapeless.Witness

object string {

  /**
    * Predicate that checks if the group count of a regex string satisfies P.
    */
  final case class GroupCount[P](p: P)

  /**
    * Predicate that checks if a regex string matches the `String` S.
    */
  final case class Matches[S](s: S)

  /**
    * Predicate that checks if a regex string contains the group named S.
    */
  final case class HasGroupName[S](s: S)

  /**
    * Predicate that checks if a regex string has no capturing groups.
    */
  type NoGroup = GroupCount[Equal[_0]]

  /**
    * Predicate that checks if a regex string has one capturing groups.
    */
  type OneGroup = GroupCount[Equal[_1]]

  object GroupCount {

    implicit def groupCountValidate[Predicate, RP](implicit vint: Validate.Aux[Int, Predicate, RP]):
      Validate.Aux[String, GroupCount[Predicate], GroupCount[vint.Res]] = {

      def helper[T](fn: T => Int) = new Validate[T, GroupCount[Predicate]] {
        override type R = GroupCount[vint.Res]

        override def validate(t: T): Res = {
          val r = vint.validate(fn(t))
          Result.fromBoolean(r.isPassed, GroupCount(r))
        }

        override def showExpr(t: T): String =
          s"/$t/.groupCount(${vint.showExpr(fn(t))})"
      }

      helper[String](target => groupCount(compile(target)))
    }
  }

  object Matches {

    implicit def matchesValidate[S <: String](implicit text: Witness.Aux[S]):
      Validate.Plain[String, Matches[S]] = {
      Validate.fromPredicate(
        t => compile(t).matcher(text.value).matches(),
        t => s"/$t/.matches(${text.value})",
        Matches(text.value)
      )
    }

  }

  object HasGroupName {

    implicit def hasGroupNameValidate[S <: String](implicit groupName: Witness.Aux[S]): Validate.Plain[String, HasGroupName[S]] =
      Validate.fromPartial(
        string => compile(string).matcher("").group(groupName.value),
        "hasGroupName",
        HasGroupName(groupName.value)
      )
  }

  lazy val compile: String => Pattern = memoize(Pattern.compile)

  def groupCount(pattern : Pattern): Int = {
    pattern.matcher("").groupCount()
  }

}
