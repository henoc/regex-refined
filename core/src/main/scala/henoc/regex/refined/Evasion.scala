package henoc.regex.refined

import java.util.regex.Pattern

import simulacrum.typeclass

import scala.util.matching.Regex

@typeclass private[refined] trait Evasion[T] {

  def clazz: Class[T]

  def field[U](instance: T, name: String): U = {
    val field = clazz.getDeclaredField(name)
    field.setAccessible(true)
    field.get(instance).asInstanceOf[U]
  }

  def method[U](instance: T, name: String): U = {
    val method = clazz.getDeclaredMethod(name)
    method.setAccessible(true)
    method.invoke(instance).asInstanceOf[U]
  }
}

object Evasion {
  implicit val regexEvasion: Evasion[Regex] = new Evasion[Regex] {
    override val clazz: Class[Regex] = classOf[Regex]
  }
  implicit val patternEvasion: Evasion[Pattern] = new Evasion[Pattern] {
    override def clazz: Class[Pattern] = classOf[Pattern]
  }
}
