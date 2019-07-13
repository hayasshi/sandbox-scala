package trees

import org.scalatest.FunSuite
import org.scalatest.DiagrammedAssertions

class BinaryTreeTest extends FunSuite with DiagrammedAssertions {

  test("smoke") {
    val l = BinaryTree(2, None, None)
    val r = BinaryTree(3, None, None)
    val tree = BinaryTree(1, Some(l), Some(r))
    
    assert(tree.data == 1)
    assert(tree.left.get == l)
    assert(tree.right.get == r)
  }

}
