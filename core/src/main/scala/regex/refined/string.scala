package henoc.regex.refined

import java.util.regex.Pattern

object string {

  final case class RegexGroupCount[T](c: T)

//  implicit def sampleValidate: Validate.Plain[String, RegexHasOneGroup] = {
//    Validate.fromPartial(str => {
//      val compiled = Pattern.compile(str)
//      compiled.matcher("").groupCount()
//    }, "regex string has one group", RegexHasOneGroup())
//  }
}
