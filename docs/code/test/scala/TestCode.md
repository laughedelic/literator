### Index

+ src
  + main
    + scala
      + [Literator.scala](../../main/scala/Literator.md)
      + [LiteratorCLI.scala](../../main/scala/LiteratorCLI.md)
      + [LiteratorParsers.scala](../../main/scala/LiteratorParsers.md)
  + test
    + scala
      + [TestCode.scala](TestCode.md)

------


```scala
package ohnosequences.tools.tests

import ohnosequences.tools.Literator._
import org.scalatest._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = literateDir(new java.io.File("src"))
    assert(errors.isEmpty, errors mkString "\n")
  }
}

```

