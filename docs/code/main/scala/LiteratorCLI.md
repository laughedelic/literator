### Index

+ src
  + main
    + scala
      + [Literator.scala](Literator.md)
      + [LiteratorCLI.scala](LiteratorCLI.md)
      + [LiteratorParsers.scala](LiteratorParsers.md)
  + test
    + scala
      + [TestCode.scala](../../test/scala/TestCode.md)

------

### Command line interface

* Input: 
  + a directory with sources or a single file
  + (optional) destination: path prefix or a file name respectively
* Output:
  + if second argument is given, file(s) with documentation
  + if not, it's set to `docs/` or `stdout` respectively


```scala
package ohnosequences.tools

import java.io._

object LiteratorCLI extends App {
  if (args.length < 1) sys.error("Need at least one argument")
  else {
    val inp = new File(args(0))
    val dest = if (args.length > 1) Some(new File(args(1))) else None
    if (inp.isDirectory) Literator.literateDir(inp, dest)
    else Literator.literateFile(inp, dest)
  }
}

```

