package henoc.regex.macros

import java.util.regex.Pattern

import scala.reflect.macros.blackbox

package object macros {
  def groupCount(regexLiteral: String): Int = macro MacroImpls.groupCount_impl

  def literal(expr: String): String = macro MacroImpls.literal_impl
}

object MacroImpls {

  private[this] def getStrLiteral(c: blackbox.Context)(expr: c.Expr[String]): String = {
    import c.universe._
    expr.tree match {
      case Literal(Constant(string: String)) => string
      case _ => throw new IllegalArgumentException(s"argument ${expr.tree} is not a string literal")
    }
  }

  def groupCount_impl(c: blackbox.Context)(regexLiteral: c.Expr[String]): c.Expr[Int] = {
    import c.universe._

    val regexString = getStrLiteral(c)(regexLiteral)

    val pattern = Pattern.compile(regexString)
    val matcher = pattern.matcher("")

    c.Expr(Literal(Constant(matcher.groupCount())))
  }

  def literal_impl(c: blackbox.Context)(expr: c.Expr[String]): c.Expr[String] = {
    import c.universe._
    val evaluated = c.eval(c.Expr[String](c.untypecheck(expr.tree)))
    c.Expr(Literal(Constant(evaluated)))
  }
}