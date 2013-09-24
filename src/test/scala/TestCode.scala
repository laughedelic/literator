package ohnosequences.tools.tests

import ohnosequences.tools._
import org.scalatest._
import java.io._

class SelfDocumentSuite extends FunSuite {

  def writeFile(file: String, text: String) = {
    Some(new PrintWriter(file)).foreach{p => p.write(text); p.close}
  }

  test("Run itself on itself's source") {
    val lit = LiteratorParsers("scala")
    val src = scala.io.Source.fromFile("src/main/scala/Literator.scala").mkString
    val result = lit.parseAll(lit.markdown, src)
    result map { writeFile("Readme.md", _) }
    println(result)
    assert(result.successful)
  }
}
