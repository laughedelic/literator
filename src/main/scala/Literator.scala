/* ### Working with files */

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
  def relativePath(f: File, base: File): String = {
    val b = if (base.isDirectory) base else base.getParentFile
    b.toPath.relativize(f.toPath).toString
  }

  // changes `.scala` extension to `.md`
  def mdExtension(name: String): String = {
    if (name.endsWith(".scala")) name.stripSuffix(".scala")+".md"
    else name
  }

  case class FileNode(f: File, t: List[FileNode]) {
    def mdLink(base: File): String =
      if (f.isDirectory) f.getCanonicalFile.getName
      else "["+ f.getCanonicalFile.getName+ "]("+ mdExtension(relativePath(f, base)) +")"

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

  def writeFile(file: String, text: String) = {
    Some(new PrintWriter(file)).foreach{p => p.write(text); p.close}
  }

  /*- This is the key function. It takes a source file, tries to parse it
    - and either outputs the result, or writes it to the specified destination. 
    - It returns parsing failure message or `None` if everthing went well.
    */
  def literateFile(f: File, destName: String = ""): Option[String] = {
    val src = scala.io.Source.fromFile(f).mkString

    val result = literator.parseAll(literator.markdown, src)
    result map {
      if (destName.isEmpty) print 
      else { text =>
        val destDir = new File(destName).getParentFile
        // `.getParentFile` may return null if you give it just a file name
        if (destDir != null && !destDir.exists) destDir.mkdirs
        writeFile(destName, text) 
      }
    }
    result match {
      case literator.NoSuccess(msg, _) => Some(f + " " + result.toString)
      case _ => None
    }
  }

  /*` This function is a wrapper, convenient for projects. It takes 
    ` the base source directory, destination path, then takes each 
    ` source file, tries to parse it, writes result to the destination
    ` and returns the list parsing errors.
    ` _Note:_ that it preserves the structure of the source directory.
    */
  def literateDir(srcBase: File, docsDest: String = ""): List[String] = {
    getFileList(srcBase).filter(_.getName.endsWith(".scala")) map { f =>

      // constructing name for the output file, creating directories, etc.
      val relative = relativePath(f, srcBase)
      val destDir = if (docsDest.isEmpty) "docs" else docsDest
      val dest = destDir.stripSuffix("/") +"/"+ relative
      val destName = mdExtension(dest)
      literateFile(f, destName)

    } flatten
  }

}

