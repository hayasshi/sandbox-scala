package trees

import scala.annotation.tailrec
import scala.collection.mutable

final case class BinaryTree[A](data: A, left: Option[BinaryTree[A]], right: Option[BinaryTree[A]])

object BinaryTree {

  /**
    * 幅優先探索
    */
  def breadthFirstSearch[A](root: BinaryTree[A])(target: A): Option[A] = {
    // for performance
    val queue = mutable.Queue.empty[BinaryTree[A]]

    @tailrec
    def loop(): Option[A] = {
      if (queue.isEmpty) None
      else {
        val elem = queue.dequeue()
        if (elem.data == target) Some(elem.data)
        else {
          elem.left.foreach(data  => queue.enqueue(data))
          elem.right.foreach(data => queue.enqueue(data))
          loop()
        }
      }
    }

    queue.enqueue(root)
    loop()
  }

  /**
    * 深さ優先探索
    */
  def depthFirstSearch[A](root: BinaryTree[A])(target: A): Option[A] = root match {
    case BinaryTree(data, None, None) =>
      if (data == target) Some(data) else None
    case BinaryTree(data, Some(lv), None) =>
      if (data == target) Some(data) else depthFirstSearch(lv)(target)
    case BinaryTree(data, None, Some(rv)) =>
      if (data == target) Some(data) else depthFirstSearch(rv)(target)
    case BinaryTree(data, Some(lv), Some(rv)) =>
      if (data == target) Some(data)
      else {
        depthFirstSearch(lv)(target) match {
          case None => depthFirstSearch(rv)(target)
          case v    => v
        }
      }
  }

}
