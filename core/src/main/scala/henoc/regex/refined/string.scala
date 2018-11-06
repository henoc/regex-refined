package henoc.regex.refined

import java.util.regex.Pattern

import eu.timepit.refined.api._
import eu.timepit.refined.api.Validate.Aux

object string {

  final case class GroupCount[P](p: P)

  object GroupCount {
    implicit def groupCountValidate[P, RP](implicit vint: Aux[Int, P, RP]): Aux[String, GroupCount[P], GroupCount[vint.Res]] = {
      def provide[T](fn: T => Int) = new Validate[T, GroupCount[P]] {
        override type R = GroupCount[vint.Res]

        override def validate(t: T): Res = {
          val r = vint.validate(fn(t))
          Result.fromBoolean(r.isPassed, GroupCount(r))
        }

        override def showExpr(t: T): String =
          s"regexStringGroupCount(${vint.showExpr(fn(t))})"
      }

      provide[String](groupCount)
    }
  }

  lazy val compile: String => Pattern = memoize(Pattern.compile)

  lazy val groupCount: String => Int = memoize(str => {
    val compiled = compile(str)
    compiled.matcher("").groupCount()
  })

}
