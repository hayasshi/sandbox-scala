package trees

import scala.annotation.tailrec
import scala.collection.mutable

final case class BinaryTree[A](data: A, left: Option[BinaryTree[A]], right: Option[BinaryTree[A]])

object BinaryTree {

  sealed abstract class SearchResult[+A]
  final case class Found[A](value: A, order: Int) extends SearchResult[A]
  final case class NotFound(order: Int)           extends SearchResult[Nothing]

  /**
    * 幅優先探索
    */
  def breadthFirstSearch[A](root: BinaryTree[A])(target: A): SearchResult[A] = {
    // for performance
    val queue = mutable.Queue.empty[BinaryTree[A]]

    @tailrec
    def loop(order: Int): SearchResult[A] = {
      if (queue.isEmpty) NotFound(order)
      else {
        val current = order + 1
        val elem    = queue.dequeue()
        if (elem.data == target) Found(elem.data, current)
        else {
          elem.left.foreach(data => queue.enqueue(data))
          elem.right.foreach(data => queue.enqueue(data))
          loop(current)
        }
      }
    }

    queue.enqueue(root)
    loop(0)
  }

  /**
    * 深さ優先探索
    */
  def depthFirstSearch[A](root: BinaryTree[A])(target: A): SearchResult[A] = {

    def loop(t: BinaryTree[A], order: Int): SearchResult[A] = {
      val current = order + 1
      t match {
        case BinaryTree(data, None, None) =>
          if (data == target) Found(data, current) else NotFound(current)
        case BinaryTree(data, Some(lv), None) =>
          if (data == target) Found(data, current) else loop(lv, current)
        case BinaryTree(data, None, Some(rv)) =>
          if (data == target) Found(data, current) else loop(rv, current)
        case BinaryTree(data, Some(lv), Some(rv)) =>
          if (data == target) Found(data, current)
          else {
            loop(lv, current) match {
              case NotFound(next) => loop(rv, next)
              case v              => v
            }
          }
      }
    }

    loop(root, 0)
  }

}
