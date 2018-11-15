package henoc.regex

import henoc.regex.refined.regex_string._
import scala.collection.mutable
import scala.reflect.macros.whitebox

package object refined {

  private[refined] def memoize[I, O](f: I => O): I => O = new mutable.HashMap[I, O]() {
    override def apply(key: I): O = getOrElseUpdate(key, f(key))
  }

  private[refined] final val D = "\"\"\""

  trait stringRegex[S <: String] {
    def unapply(s: String): Any = macro StringRegexUnapplyBundle.impl[S]
  }

  class StringRegexUnapplyBundle(val c: whitebox.Context) {
    import c.universe._
    def impl[S: c.WeakTypeTag](s: Tree): Tree = {
      val regex = weakTypeOf[S] match {
        case ConstantType(Constant(regex: String)) => regex
        case other => c.abort(c.enclosingPosition, s"unapply of stringRegex is only available for literal type, but found: $other")
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
