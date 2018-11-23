package henoc.regex.stdlib

import sun.security.action.GetPropertyAction
import sun.text.Normalizer

/**
  * Invoke internal libraries from scala to suppress warnings.
  */
private[stdlib] object InvokeInternalLibraries {
  def getCombiningClass(c: Int): Int = Normalizer.getCombiningClass(c)
  def newGetPropertyAction(s: String) = new GetPropertyAction(s)
}
