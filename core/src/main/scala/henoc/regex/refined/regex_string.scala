package henoc.regex.refined

import java.util.regex.PatternSyntaxException

import eu.timepit.refined._
import eu.timepit.refined.string._
import eu.timepit.refined.api._
import eu.timepit.refined.api.Validate
import eu.timepit.refined.generic.Equal
import shapeless.Nat.{_0, _1}
import shapeless.Witness
import eu.timepit.refined.api.Validate.Plain
import eu.timepit.refined.boolean.And
import henoc.regex.stdlib.PatternExtra
import javax.script.ScriptEngineManager

import scala.util.{Failure, Success, Try}

object regex_string {

  /**
    * Predicate that checks if the group count of a regex string satisfies P.
    */
  final case class GroupCount[P](p: P)

  /**
    * Predicate that checks if a regex string matches the `String` S.
    */
  final case class Matches[S](s: S)

  /**
    * Predicate that checks if a regex string contains the group name S.
    */
  final case class HasGroupName[S](s: S)

  /**
    * Predicate that checks if a regex string uses the match flags S. (S is `[idmsuxU]+`)
    */
  final case class MatchFlags[S](s: S)

  /**
    * Predicate that checks if a regex string is valid for JavaScript regex.
    */
  final case class JsRegex()

  /**
    * Predicate that checks if a regex string has no capturing groups.
    */
  type NoGroup = GroupCount[Equal[_0]]

  /**
    * Predicate that checks if a regex string has one capturing groups.
    */
  type OneGroup = GroupCount[Equal[_1]]

  /**
    * Predicate that checks if a regex string is JavaScript compatible.
    */
  type JsRegexCompatible = Regex And JsRegex

  object GroupCount {

    implicit def groupCountValidate[Predicate, RP](implicit vint: Validate.Aux[Int, Predicate, RP]):
      Validate.Aux[String, GroupCount[Predicate], GroupCount[vint.Res]] = {

      def helper[T](fn: T => Unit => Int) = new Validate[T, GroupCount[Predicate]] {
        override type R = GroupCount[vint.Res]

        override def validate(t: T): Res = {
          try {
            val r = vint.validate(fn(t)(()))
            Result.fromBoolean(r.isPassed, GroupCount(r))
          } catch {
            case _: PatternSyntaxException => Failed(GroupCount(vint.validate(0)))
          }
        }

        override def showExpr(t: T): String = Try(fn(t)(())) match {
          case Success(i) => s"groupCount(/$t/): ${vint.showExpr(i)}"
          case Failure(e) => s"Pattern.compile: ${e.getMessage}"
        }

      }

      helper[String](target => _ => PatternExtra.compile(target).groupCount())
    }
  }

  object Matches {

    implicit def matchesValidate[S <: String](implicit text: Witness.Aux[S]):
      Validate.Plain[String, Matches[S]] = {
      fromPredicateWithRegex(
        p => p.matches(text.value),
        p => s"/$p/.matches($D${text.value}$D)",
        Matches(text.value)
      )
    }

  }

  object HasGroupName {

    implicit def hasGroupNameValidate[S <: String](implicit groupName: Witness.Aux[S]): Validate.Plain[String, HasGroupName[S]] =
      fromPredicateWithRegex(
        p => p.namedGroups().containsKey(groupName.value),
        p => s"/$p/.hasGroupName($D${groupName.value}$D)",
        HasGroupName(groupName.value)
      )

  }

  object MatchFlags {

    implicit def matchFlagsValidate[S <: String](implicit flags: Witness.Aux[S]): Validate.Plain[String, MatchFlags[S]] = {
      val flagChars = refineV[MatchesRegex[W.`"[idmsuxU]+"`.T]].unsafeFrom(flags.value: String).value
      val flagsInt = PatternExtra.compile(s"(?$flagChars)").flags()
      fromPredicateWithRegex(
        p => (p.flags() & flagsInt) == flagsInt,
        p => s"/$p/.useMatchFlag($D${flags.value}$D)",
        MatchFlags(flags.value)
      )
    }

  }

  object JsRegex {

    implicit def jsRegexValidate: Validate.Plain[String, JsRegex] = {
      val engine = new ScriptEngineManager().getEngineByName("js")
      Validate.fromPartial(t => engine.eval(s"/$t/"), "jsRegex", JsRegex())
    }

  }

  private[refined] def fromPredicateWithRegex[P](f: PatternExtra => Boolean, showExpr: PatternExtra => String, p: P): Plain[String, P] = {
    val g = showExpr
    new Validate[String, P] {
      override type R = P
      override def validate(t: String): Res = {
        try {
          Result.fromBoolean(f(PatternExtra.compile(t)), p)
        } catch {
          case _: PatternSyntaxException => Failed(p)
        }
      }
      override def showExpr(t: String): String = Try(PatternExtra.compile(t)) match {
        case Success(ptn) => g(ptn)
        case Failure(e) => s"Pattern.compile: ${e.getMessage}"
      }
    }
  }

}
