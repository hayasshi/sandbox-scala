package trees

import org.scalatest.FunSuite
import org.scalatest.DiagrammedAssertions
import trees.BinaryTree.Found
import trees.BinaryTree.NotFound

class BinaryTreeTest extends FunSuite with DiagrammedAssertions {

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

  test("bfs - 検索対象が含まれていれば値と検索計算量を返す") {
    assert(BinaryTree.breadthFirstSearch(root)(4) == Found(4, 1))
    assert(BinaryTree.breadthFirstSearch(root)(2) == Found(2, 2))
    assert(BinaryTree.breadthFirstSearch(root)(3) == Found(3, 3))
    assert(BinaryTree.breadthFirstSearch(root)(5) == Found(5, 4))
    assert(BinaryTree.breadthFirstSearch(root)(1) == Found(1, 5))
  }

  test("bfs - 検索対象が含まれていなければ全検索計算量のみを返す") {
    assert(BinaryTree.breadthFirstSearch(root)(6) == NotFound(5))
    assert(BinaryTree.breadthFirstSearch(root)(0) == NotFound(5))
  }

  test("dfs - 検索対象が含まれていれば値と検索計算量を返す") {
    assert(BinaryTree.depthFirstSearch(root)(4) == Found(4, 1))
    assert(BinaryTree.depthFirstSearch(root)(2) == Found(2, 2))
    assert(BinaryTree.depthFirstSearch(root)(3) == Found(3, 4))
    assert(BinaryTree.depthFirstSearch(root)(5) == Found(5, 3))
    assert(BinaryTree.depthFirstSearch(root)(1) == Found(1, 5))
  }

  test("dfs - 検索対象が含まれていなければ値を返さない") {
    assert(BinaryTree.depthFirstSearch(root)(6) == NotFound(5))
    assert(BinaryTree.depthFirstSearch(root)(0) == NotFound(5))
  }

}
