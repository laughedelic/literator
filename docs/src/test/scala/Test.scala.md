
```scala
package laughedelic.literator.tests

import laughedelic.literator.lib._
import laughedelic.literator.lib.FileUtils._
import org.scalatest._

class SelfDocumentSuite extends FunSuite {
  test("Run itself on itself's source") {
    val errors = file("src/main/scala/").literate(Some(file("docs/src/")))
    assert(errors.isEmpty, errors mkString "\n")
  }
}

```




[main/scala/lib/FileUtils.scala]: ../../main/scala/lib/FileUtils.scala.md
[main/scala/lib/LanguageMap.scala]: ../../main/scala/lib/LanguageMap.scala.md
[main/scala/lib/LiteratorParsers.scala]: ../../main/scala/lib/LiteratorParsers.scala.md
[main/scala/lib/package.scala]: ../../main/scala/lib/package.scala.md
[main/scala/plugin/LiteratorPlugin.scala]: ../../main/scala/plugin/LiteratorPlugin.scala.md
[main/scala/Readme.md]: ../../main/scala/Readme.md.md
[test/scala/Test.scala]: Test.scala.md