### Working with files

```scala
package ohnosequences.tools

import java.io._
import java.nio.file.Path

import literator.LanguageMap._
import literator.FileUtils._

package object literator {

  implicit class FileLiterator(root: File) {
```

This is the key function. If the source file is a directory, it traverses it, takes all 
children, parses each and writes a correcponding markdown file. If parser encounters some 
errors, it returns themin a list.


```scala
    def literate(
          destBase: Option[File] = None
        , withIndex: Boolean = true
        ): List[String] = {

      val fileList = root getFileList { _.isSource }

      fileList flatMap { child =>

        val base: File = destBase.getOrElse(new File("docs/src")).getCanonicalFile
        val relative: Path = child.getCanonicalFile.getParentFile.relativePath(root)
        val destDir: File = new File(base, relative.toString)
        val index = root getFileTree { f => f.isDirectory || f.isSource } match {
            case Some(ix) if withIndex => Seq("------", "### Index", ix) mkString "\n\n"
            case _ => ""
          }
        val linksList = fileList map { f =>
            "["+f.relativePath(root).toString+"]: "+f.relativePath(child).toString+".md"
          } mkString("\n")

        langMap.get(child.ext) flatMap { lang =>

          val literator = LiteratorParsers(lang)
          val src = scala.io.Source.fromFile(child).mkString
          val parsed = literator.parseAll(literator.markdown, src) 

          parsed match {
            case literator.NoSuccess(msg, _) => Some(child + " " + parsed)
            case literator.Success(result, _) => {
              val text = Seq(result, index, linksList) mkString "\n\n"

              if (!destDir.exists) destDir.mkdirs
              new File(destDir, child.name+".md").write(text) 

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
