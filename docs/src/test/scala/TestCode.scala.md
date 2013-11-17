
```scala
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

```


------

### Index

+ src
  + main
    + scala
      + [FileUtils.scala](../../main/scala/FileUtils.scala.md)
      + [LanguageMap.scala](../../main/scala/LanguageMap.scala.md)
      + [LiteratorCLI.scala](../../main/scala/LiteratorCLI.scala.md)
      + [LiteratorParsers.scala](../../main/scala/LiteratorParsers.scala.md)
      + [package.scala](../../main/scala/package.scala.md)
  + test
    + scala
      + [TestCode.scala](TestCode.scala.md)
