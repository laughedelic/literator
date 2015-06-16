
```scala
package laughedelic.literator.tests

import laughedelic.literator.lib._
import laughedelic.literator.lib.FileUtils._
import org.scalatest._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = file("lib/src/main/scala/").literate(Some(file("docs/src/lib"))) ++
                 file("plugin/src/main/scala/").literate(Some(file("docs/src/plugin")))
    assert(errors.isEmpty, errors mkString "\n")
  }
}

```




[main/scala/FileUtils.scala]: ../../main/scala/FileUtils.scala.md
[main/scala/LanguageMap.scala]: ../../main/scala/LanguageMap.scala.md
[main/scala/LiteratorParsers.scala]: ../../main/scala/LiteratorParsers.scala.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[main/scala/Readme.md]: ../../main/scala/Readme.md.md
[test/scala/Test.scala]: Test.scala.md