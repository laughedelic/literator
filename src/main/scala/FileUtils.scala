/* ## Generic file utilities */

package ohnosequences.tools.literator

import java.io._
import java.nio.file.Path
import java.nio.file.Path._
import LanguageMap._

/* This type represents a node of file hierarchy tree */
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

/* Let's extend `File` type with some useful functions */
object FileUtils {

  // just an alias:
  def file(path: String): File = new File(path)

  implicit class FileOps(file: File) {

    // returns path of `file` relatively to `base`
    def relativePath(base: File): Path = {
      val b = if (base.isDirectory) base.getCanonicalFile 
              else base.getCanonicalFile.getParentFile
      b.toPath.relativize(file.getCanonicalFile.toPath)
    }

    // name (last part after /) and extension
    def name: String = file.getCanonicalFile.getName
    def ext: String = if (file.isDirectory) "" else file.name.split("\\.").last

    def isSource: Boolean = langMap.isDefinedAt(file.ext)

    // traverses recursively and lists all _files_ passing the filter
    def getFileList(filter: (File => Boolean) = (_ => true)): List[File] =
      if (file.isDirectory) file.listFiles.toList.flatMap(_.getFileList(filter))
      else if (filter(file)) List(file) else List()

    // traverses recursively and builds file hierarchy tree
    def getFileTree(filter: (File => Boolean) = (_ => true)): Option[FileNode] =
      if (!filter(file)) None
      else Some(FileNode(file, 
            if (file.isDirectory) file.listFiles.toList.map(_.getFileTree(filter)).flatten 
            else List()
           ))

    // just writes to the file
    def write(text: String) = {
      import sys.process._
      Seq("echo", text) #> file !
    }

  }
}
