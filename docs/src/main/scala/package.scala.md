### Working with files

```scala
package laughedelic.literator

import java.io._
import java.nio.file.Path

import lib.LanguageMap._
import lib.FileUtils._

package object lib {

  implicit class FileLiterator(root: File) {
```

Checks that the file has a known source format

```scala
    def isSource: Boolean = langMap.isDefinedAt(root.ext)
    def isMarkdown: Boolean = root.ext match {
      case "md" | "mkd" | "mdown" | "markdown" => true
      case _ => false
    }
```

This is the key function. If the source file is a directory, it traverses it, takes all 
children, parses each and writes a correcponding markdown file. If parser encounters some 
errors, it returns them in a list. 


```scala
    def literate(
          destBase: Option[File] = None
        , withIndex: Boolean = true
        ): List[String] = {
```

First we can generate index section

```scala
      val index = root getFileTree { f => f.isDirectory || f.isSource } match {
          case Some(ix) if withIndex => Seq("------", "### Index", ix) mkString "\n\n"
          case _ => ""
        }
```

Then we start with traversing list of source files

```scala
      val fileList = root getFileList { f => f.isSource || f.isMarkdown }

      def writeResult(source: File, name: String, text: String): Unit = {
        destBase map { base =>
          val relative: Path = source.getCanonicalFile.getParentFile.relativePath(root)
          val destDir: File = new File(base.getCanonicalFile, relative.toString)
          if (!destDir.exists) destDir.mkdirs
          new File(destDir, name).write(text) 
        }
      }

      // TODO: this code is bad structured: look at all those } } } in the end... 
      fileList flatMap { child =>
```

And for each of them we generate a block of relative links

```scala
        val linksList = fileList map { f =>
            "["+f.relativePath(root).toString+"]: "+f.relativePath(child).toString+".md"
          } mkString("\n")

        child.ext match {
          case "md" | "mkd" | "mdown" | "markdown" => {
            val text = Seq(child.read, linksList) mkString "\n\n"
            writeResult(child, child.name, text)
            None
          }
          case _ => {

            langMap.get(child.ext) flatMap { lang =>
```

Knowing the language of the source we can parse it

```scala
              val literator = LiteratorParsers(lang)
              val parsed = literator.parseAll(literator.markdown, child.read) 

              parsed match {
                case literator.NoSuccess(msg, _) => Some(s"${child} ${parsed}")
                case literator.Success(result, _) => {
                  val text = Seq(result, index, linksList) mkString "\n\n"
```

And if we parsed something, we write it to the file

```scala
                  writeResult(child, child.name+".md", text)
                  None
                }
              }
            }
          }
        }
      }
    }
  }
}


```




[main/scala/FileUtils.scala]: FileUtils.scala.md
[main/scala/LanguageMap.scala]: LanguageMap.scala.md
[main/scala/LiteratorParsers.scala]: LiteratorParsers.scala.md
[main/scala/package.scala]: package.scala.md
[main/scala/Readme.md]: Readme.md.md
[test/scala/Test.scala]: ../../test/scala/Test.scala.md