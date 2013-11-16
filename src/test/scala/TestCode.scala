package ohnosequences.tools.literator.tests

import ohnosequences.tools.literator._
import ohnosequences.tools.literator.FileUtils._
import org.scalatest._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = file("src").literate()
    assert(errors.isEmpty, errors mkString "\n")
  }
}
