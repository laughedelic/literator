## Generic file utilities

```scala
package laughedelic.literator.lib

import java.io._
import java.nio.file.Path
import java.nio.file.Path._
import LanguageMap._
```

This type represents a node of file hierarchy tree

```scala
case class FileNode(f: File, t: List[FileNode]) {
  import FileUtils._

  def link(base: File): String =
    if (f.isSource) "["+f.name+"]["+f.relativePath(base)+"]"
    else f.name

  def listTree(base: File): List[String] = {
    ("+ " + link(base)) :: 
    t.flatMap{ i: FileNode => i.listTree(base).map{ s: String => "  " + s } }
  }

  override def toString: String = listTree(f).mkString("\n")
}
```

Let's extend `File` type with some useful functions

```scala
object FileUtils {

  // just an alias:
  def file(path: String): File = new File(path)

  implicit class FileOps(file: File) {
```

Returning path of `file` relatively to `base`

```scala
    def relativePath(base: File): Path = {
      val b = if (base.isDirectory) base.getCanonicalFile 
              else base.getCanonicalFile.getParentFile
      b.toPath.relativize(file.getCanonicalFile.toPath)
    }
```

Name (last part after `/`) and extension

```scala
    def name: String = file.getCanonicalFile.getName
    def ext: String = if (file.isDirectory) "" else file.name.split("\\.").last
```

Traversing recursively and listing all _files_ passing the filter

```scala
    def getFileList(filter: (File => Boolean) = (_ => true)): List[File] =
      if (file.isDirectory) file.listFiles.toList.flatMap(_.getFileList(filter))
      else if (filter(file)) List(file) else List()
```

Traversing recursively and building file hierarchy tree

```scala
    def getFileTree(filter: (File => Boolean) = (_ => true)): Option[FileNode] =
      if (!filter(file)) None
      else Some(FileNode(file, 
            if (file.isDirectory) file.listFiles.toList.map(_.getFileTree(filter)).flatten 
            else List()
           ))
```

Read the contents of the file

```scala
    def read: String = scala.io.Source.fromFile(file, "UTF-8").mkString
```

Just writing to the file

```scala
    def write(text: String) = {
      Some(new PrintWriter(file)).foreach{p => p.write(text); p.close}
    }

  }
}

```


------

### Index

+ scala
  + [FileUtils.scala][FileUtils.scala]
  + [LanguageMap.scala][LanguageMap.scala]
  + [LiteratorParsers.scala][LiteratorParsers.scala]
  + [package.scala][package.scala]

[FileUtils.scala]: FileUtils.scala.md
[LanguageMap.scala]: LanguageMap.scala.md
[LiteratorParsers.scala]: LiteratorParsers.scala.md
[package.scala]: package.scala.md
[Readme.markdown]: Readme.markdown.md