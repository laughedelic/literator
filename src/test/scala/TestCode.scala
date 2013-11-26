package ohnosequences.tools.literator.tests

import ohnosequences.literator.lib._
import ohnosequences.literator.lib.FileUtils._
import org.scalatest._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = 
           file("lib/src/main/scala/").literate(Some(file("docs/src/lib"))) ++
           file("app/src/main/scala/").literate(Some(file("docs/src/app"))) ++
        file("plugin/src/main/scala/").literate(Some(file("docs/src/plugin")))
    assert(errors.isEmpty, errors mkString "\n")
  }
}
