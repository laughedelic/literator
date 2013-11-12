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

### Working with files

```scala
package ohnosequences.tools

import java.io._
// import java.nio.file.File._
import java.nio.file.Path
import java.nio.file.Path._

object Literator {

  // TODO: determine language from the file extension
  val literator = LiteratorParsers("scala")

  // traverses recursively given directory and lists all files
  def getFileList(f: File): List[File] =
    f :: (if (f.isDirectory) f.listFiles.toList.flatMap(getFileList) 
          else List())

  // returns path of `f` relatively to `base` as a `String`
  def relativePath(f: File, base: File): Path = {
    val b = if (base.isDirectory) base else base.getParentFile
    b.toPath.relativize(f.toPath)
  }

  case class FileNode(f: File, t: List[FileNode]) {
    def mdLink(base: File): String = {
      val name = f.getCanonicalFile.getName
      if (name.endsWith(".scala")) 
        "["+name+"]("+relativePath(f, base).toString.stripSuffix(".scala")+".md)"
      else name
    }

    def tree(base: File): List[String] = {
      ("+ " + mdLink(base)) :: 
      t.flatMap{ i: FileNode => i.tree(base).map{ s: String => "  " + s } }
    }

    override def toString: String = tree(f).mkString("\n")
  }

  def getFileTree(root: File): FileNode =
    FileNode(root, 
             if (root.isDirectory) root.listFiles.toList.map(getFileTree) 
             else List()
            )

  def writeFile(file: File, text: String) = {
    import sys.process._
    Seq("echo", text) #> file !
  }
```

This is the key function. It takes a source file, tries to parse it
and either outputs the result, or writes it to the specified destination. 
It returns parsing failure message or `None` if everthing went well.

```scala
  def literateFile(
        file: File
      , destDir: Option[File] = None
      , index: Option[FileNode] = None
      ): Option[String] = {
    val src = scala.io.Source.fromFile(file).mkString
    val parsed = literator.parseAll(literator.markdown, src) 
    parsed match {
      case literator.NoSuccess(msg, _) => Some(file + " " + parsed.toString)
      case literator.Success(result, _) => {
        val text = index match {
          case None => result
          case Some(n) => Seq(
              "### Index"
            , n.tree(file).mkString("\n")
            , "------"
            , result
            ).mkString("\n\n")
        }
        destDir match {
          case None => print(text)
          case Some(dir) => {
            if (!dir.exists) dir.mkdirs
            val name = file.getCanonicalFile.getName.stripSuffix(".scala")+".md"
            writeFile(new File(dir, name), text) 
          }
        }
        None
      }
    }
  }
```

This function is a wrapper, convenient for projects. It takes 
the base source directory, destination path, then takes each 
source file, tries to parse it, writes result to the destination
and returns the list parsing errors.
_Note:_ that it preserves the structure of the source directory.

```scala
  def literateDir(
        srcBase: File
      , destBase: Option[File] = None
      , withIndex: Boolean = true
      ): List[String] = {

    getFileList(srcBase).filter(_.getName.endsWith(".scala")) map { f =>
      val base: File = destBase.getOrElse(new File("docs/code")).getCanonicalFile
      val relative: Path = relativePath(f.getParentFile, srcBase)
      val dest: File = new File(base, relative.toString)
      literateFile(f, Some(dest), if (withIndex) Some(getFileTree(srcBase)) else None)
    } flatten

  }

}


```

