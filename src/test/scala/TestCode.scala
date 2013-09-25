package ohnosequences.tools.tests

import ohnosequences.tools._
import org.scalatest._
import java.io._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    // val results = Literator.literateDir(new File("src/main/scala/"))
    // assert(results forall (_.successful))

    val mainSource = new File("src/main/scala/Literator.scala")
    val result = Literator.literateFile(mainSource, "Readme.md")
    // println(result)
    assert(result.successful)
  }
}
