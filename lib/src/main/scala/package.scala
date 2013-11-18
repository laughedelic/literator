/* ### Working with files */

package ohnosequences.literator

import java.io._
import java.nio.file.Path

import lib.LanguageMap._
import lib.FileUtils._

package object lib {

  implicit class FileLiterator(root: File) {

    /*  This is the key function. If the source file is a directory, it traverses it, takes all 
        children, parses each and writes a correcponding markdown file. If parser encounters some 
        errors, it returns themin a list. 
    */
    def literate(
          destBase: Option[File] = None
        , withIndex: Boolean = true
        ): List[String] = {

      val fileList = root getFileList { _.isSource }

      fileList flatMap { child =>

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

              destBase map { db =>
                val base: File = db.getCanonicalFile
                val relative: Path = child.getCanonicalFile.getParentFile.relativePath(root)
                val destDir: File = new File(base, relative.toString)
                if (!destDir.exists) destDir.mkdirs
                new File(destDir, child.name+".md").write(text) 
              }

              None
            }
          }

        }

      }
    }

  }

}

