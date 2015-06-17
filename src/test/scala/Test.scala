package laughedelic.literator.tests

import laughedelic.literator.lib._
import laughedelic.literator.lib.FileUtils._
import org.scalatest._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = file("src/main/scala/").literate(Some(file("docs/src/")))
    assert(errors.isEmpty, errors mkString "\n")
  }
}
