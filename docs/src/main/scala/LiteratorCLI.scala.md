### Command line interface

* Input: 
  + a directory with sources or a single file
  + (optional) destination: path prefix or a file name respectively
* Output:
  + if second argument is given, file(s) with documentation
  + if not, it's set to `docs/` or `stdout` respectively


```scala
package ohnosequences.tools.literator

import java.io._
import ohnosequences.tools.literator.FileUtils._

object LiteratorCLI extends App {
  if (args.length < 1) sys.error("Need at least one argument")
  else file(args(0)).literate(if (args.length > 1) Some(file(args(1))) else None)
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

[main/scala/FileUtils.scala]: FileUtils.scala.md
[main/scala/LanguageMap.scala]: LanguageMap.scala.md
[main/scala/LiteratorCLI.scala]: LiteratorCLI.scala.md
[main/scala/LiteratorParsers.scala]: LiteratorParsers.scala.md
[main/scala/package.scala]: package.scala.md
[test/scala/TestCode.scala]: ../../test/scala/TestCode.scala.md
