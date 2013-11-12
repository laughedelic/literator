package ohnosequences.tools.tests

import ohnosequences.tools.Literator._
import org.scalatest._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = literateDir(new java.io.File("src"))
    assert(errors.isEmpty, errors mkString "\n")
  }
}
