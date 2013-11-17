
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
      + [FileUtils.scala][main/scala/FileUtils.scala]
      + [LanguageMap.scala][main/scala/LanguageMap.scala]
      + [LiteratorCLI.scala][main/scala/LiteratorCLI.scala]
      + [LiteratorParsers.scala][main/scala/LiteratorParsers.scala]
      + [package.scala][main/scala/package.scala]
  + test
    + scala
      + [TestCode.scala][test/scala/TestCode.scala]

[main/scala/FileUtils.scala]: ../../main/scala/FileUtils.scala.md
[main/scala/LanguageMap.scala]: ../../main/scala/LanguageMap.scala.md
[main/scala/LiteratorCLI.scala]: ../../main/scala/LiteratorCLI.scala.md
[main/scala/LiteratorParsers.scala]: ../../main/scala/LiteratorParsers.scala.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[test/scala/TestCode.scala]: TestCode.scala.md
