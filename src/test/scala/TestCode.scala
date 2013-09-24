package laughedelic.tools.tests

import laughedelic.tools._
import org.scalatest._
import java.io._
import scala.io._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val lit = LiteratorParsers("scala")
    val src = scala.io.Source.fromFile("src/main/scala/Literator.scala").mkString
    val md = lit.parse(lit.markdown, src)
    md map { text =>
      Some(new PrintWriter("Readme.md")).foreach{p => p.write(text); p.close}
    }
  }
}
