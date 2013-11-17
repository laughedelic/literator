### Working with files

```scala
package ohnosequences.tools

import java.io._
import java.nio.file.Path

import literator.LanguageMap._
import literator.FileUtils._

package object literator {

  implicit class FileLiterator(file: File) {
```

This is the key function. If the source file is a directory, it traverses it, takes all 
children, parses each and writes a correcponding markdown file. If parser encounters some 
errors, it returns themin a list.


```scala
    def literate(
          destBase: Option[File] = None
        , withIndex: Boolean = true
        ): List[String] = {

      file getFileList { f => langMap.isDefinedAt(f.ext) } flatMap { child =>

        val base: File = destBase.getOrElse(new File("docs/src")).getCanonicalFile
        val relative: Path = child.getCanonicalFile.getParentFile.relativePath(file)
        val dest: File = new File(base, relative.toString)
        val index = if (!withIndex) None
                    else file getFileTree { f => f.isDirectory || langMap.isDefinedAt(f.ext) }

        langMap.get(child.ext) flatMap { lang =>

          val literator = LiteratorParsers(lang)
          val src = scala.io.Source.fromFile(child).mkString
          val parsed = literator.parseAll(literator.markdown, src) 

          parsed match {
            case literator.NoSuccess(msg, _) => Some(child + " " + parsed)
            case literator.Success(result, _) => {

              val text = index match {
                case None => result
                case Some(ix) => Seq(
                    result
                  , "------"
                  , "### Index"
                  , ix.tree(child).mkString("\n")
                  ).mkString("\n\n")
              }

              if (!dest.exists) dest.mkdirs
              new File(dest, child.name+".md").write(text) 

              None
            }
          }

        }

      }
    }

  }

}


```


------

### Index

+ src
  + main
    + scala
      + [FileUtils.scala](FileUtils.scala.md)
      + [LanguageMap.scala](LanguageMap.scala.md)
      + [LiteratorCLI.scala](LiteratorCLI.scala.md)
      + [LiteratorParsers.scala](LiteratorParsers.scala.md)
      + [package.scala](package.scala.md)
  + test
    + scala
      + [TestCode.scala](../../test/scala/TestCode.scala.md)
