package ohnosequences.tools.tests

import ohnosequences.tools._
import org.scalatest._
import java.io._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = Literator.literateDir(new File("src/main/scala/"), "docs/code")
    // println(errors)
    assert(errors.isEmpty, errors mkString "\n")
  }
}
