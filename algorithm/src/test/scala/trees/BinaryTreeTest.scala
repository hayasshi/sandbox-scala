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

  /**
   *       4
   *      / \
   *     2   3
   *    /     \
   *   5       1
   */
  val _5 = BinaryTree(5, None, None)
  val _1 = BinaryTree(1, None, None)
  val _2 = BinaryTree(2, Some(_5), None)
  val _3 = BinaryTree(3, None, Some(_1))
  val _4 = BinaryTree(4, Some(_2), Some(_3))
  val root = _4

  test("bfs - 検索対象が含まれていれば値を返す") {
    assert(BinaryTree.breadthFirstSearch(root)(4) == Some(4))
    assert(BinaryTree.breadthFirstSearch(root)(2) == Some(2))
    assert(BinaryTree.breadthFirstSearch(root)(3) == Some(3))
    assert(BinaryTree.breadthFirstSearch(root)(5) == Some(5))
    assert(BinaryTree.breadthFirstSearch(root)(1) == Some(1))
  }

  test("bfs - 検索対象が含まれていなければ値を返さない") {
    assert(BinaryTree.breadthFirstSearch(root)(6) == None)
    assert(BinaryTree.breadthFirstSearch(root)(0) == None)
  }

  test("dfs - 検索対象が含まれていれば値を返す") {
    assert(BinaryTree.depthFirstSearch(root)(4) == Some(4))
    assert(BinaryTree.depthFirstSearch(root)(2) == Some(2))
    assert(BinaryTree.depthFirstSearch(root)(3) == Some(3))
    assert(BinaryTree.depthFirstSearch(root)(5) == Some(5))
    assert(BinaryTree.depthFirstSearch(root)(1) == Some(1))
  }

  test("dfs - 検索対象が含まれていなければ値を返さない") {
    assert(BinaryTree.depthFirstSearch(root)(6) == None)
    assert(BinaryTree.depthFirstSearch(root)(0) == None)
  }

}
