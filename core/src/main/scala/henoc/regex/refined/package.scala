package henoc.regex

import scala.collection.mutable

package object refined {

  private[refined] def memoize[I, O](f: I => O): I => O = new mutable.HashMap[I, O]() {
    override def apply(key: I): O = getOrElseUpdate(key, f(key))
  }

  private[refined] final val D = "\"\"\""

}
