package henoc.regex

import scala.language.dynamics
import henoc.regex.refined.regex_string._
import scala.reflect.macros.whitebox

package object macros {

  /**
    * Provide a regex string interpolator for pattern matching.
    * {{{
    * "2018-11-18" match {
    *   case regex"""(\d+$year)-(\d+$month)-(\d+$day)""" => println(s"year = $year, month = $month, day = $day")
    *   case _ => println("no!")
    * }
    * }}}
    *
    * The variable positions of interpolator is not concerned, so you can also write:
    * {{{
    *   regex"""(\d+)-(\d+)-(\d+)$year$month$day"""
    *   regex"""(?x)   (\d+)-(\d+)-(\d+)    # $year, $month, $day"""
    * }}}
    */
  implicit class RegexContext(sc: StringContext) {
    def regex: RegexContextExtractor = macro provideExtractorInstance
  }

  def provideExtractorInstance(c: whitebox.Context): c.Tree = {
    import c.universe._

    c.prefix.tree match {
      case Apply(_, List(Apply(_, rawParts))) =>
        val parts = rawParts map { case Literal(Constant(const: String)) => const }
        q"new RegexContextExtractor(${Literal(Constant(parts.mkString))})"
      case _ =>
        c.abort(c.enclosingPosition, "Invalid use of regex string extractor")
    }
  }

  def provideExtractorImitator(c: whitebox.Context)(s: c.Tree): c.Tree = {
    import c.universe._

    val regex = c.prefix.tree match {
      case Apply(_, List(Literal(Constant(const: String)))) => const
      case _ => c.abort(c.enclosingPosition, "Invalid use of regex string extractor")
    }
    val count = groupCount(compile(regex))
    val imitationMethods = (1 to count)
      .map(i => q"def ${TermName("_" + i)} = matched.get(${Literal(Constant(i - 1))})")
      .toList

    q"""
       new {
         var matched: Option[Array[String]] = None
         def isEmpty = matched.isEmpty
         def get = this
         ..$imitationMethods
         def unapply(s: String) = {
           matched = ${Literal(Constant(regex))}.r.unapplySeq(s).map(_.toArray)
           this
         }
       }.unapply($s)
     """
  }

  class RegexContextExtractor(regex: String) {
    def unapply(s: String): Any = macro provideExtractorImitator
  }
}
