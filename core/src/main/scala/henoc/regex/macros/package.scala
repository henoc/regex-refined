package henoc.regex

import henoc.regex.refined.regex_string._
import scala.reflect.macros.whitebox

package object macros {

  implicit class RegexContext(sc: StringContext) {
    /**
      * Provide a regex string interpolator for pattern matching.
      * {{{
      *   "2018-11-18" match {
      *     case r"""(\d+$year)-(\d+$month)-(\d+$day)""" => println(s"year = $year, month = $month, day = $day")
      *     case _ => println("no!")
      *   }
      * }}}
      *
      * The variable positions of interpolator is not concerned, so you can also write:
      * {{{
      *   r"""(\d+)-(\d+)-(\d+)$year$month$day"""
      *   r"""(?x)   (\d+)-(\d+)-(\d+)    # $year, $month, $day"""
      * }}}
      */
    def r: RegexContextExtractor = new RegexContextExtractor(sc)
  }

  class RegexContextExtractor(sc: StringContext) {
    def unapply(s: String): Any = macro MacroImpls.provideExtractorImitator
  }

  private[macros] object MacroImpls {

    def provideExtractorImitator(c: whitebox.Context)(s: c.Tree): c.Tree = {
      import c.universe._

      val regex = c.prefix.tree match {
        case Select(Apply(_, List(Apply(_, rawParts))), _) => rawParts.map{ case q"${const: String}" => const }.mkString
        case _ => c.abort(c.enclosingPosition, s"Invalid use of regex string extractor")
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
  }
}
