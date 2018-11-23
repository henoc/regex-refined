package henoc.regex.stdlib

import java.util
import java.util.regex.{PatternSyntaxException => PatternExceptionOriginal}

import scala.collection.immutable.Queue
import scala.util.Try

/**
  * Wrapper class for Pattern.
  */
private[regex] class PatternExtra(pattern: Pattern) {

  override def toString: String = pattern.toString

  def namedGroups(): util.Map[String, Integer] = pattern.namedGroups()

  def groupCount(): Int = pattern.matcher("").groupCount()

  def flags(): Int = pattern.flags()

  def matches(text: String): Boolean = pattern.matcher(text).matches()

  def isFullMatchPattern: Boolean = (nodeList.head, nodeList.drop(nodeList.size - 2).head) match {
    case (_: Pattern.Begin, _: Pattern.Dollar) | (_: Pattern.Begin, _: Pattern.UnixDollar) => true
    case _ => false
  }

  private[this] lazy val nodeList: Queue[Pattern.Node] = {
    def helper(node: Pattern.Node, acc: Queue[Pattern.Node] = Queue.empty): Queue[Pattern.Node] = node match {
      case lastNode: Pattern.LastNode => acc :+ lastNode
      case _ => helper(node.next, acc :+ node)
    }

    helper(pattern.root)
  }

}
private[regex] object PatternExtra {

  def compile(regex: String): PatternExtra =
    Try(new PatternExtra(Pattern.compile(regex))).recover {
      case e: PatternSyntaxException => throw new PatternExceptionOriginal(e.getDescription, e.getPattern, e.getIndex)
    }.get

}
