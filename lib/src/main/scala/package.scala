/* ### Working with files */

package ohnosequences.literator

import java.io._
import java.nio.file.Path

import lib.LanguageMap._
import lib.FileUtils._

package object lib {

  implicit class FileLiterator(root: File) {

    /* Checks that the file has a known source format */
    def isSource: Boolean = langMap.isDefinedAt(root.ext)

    /*  This is the key function. If the source file is a directory, it traverses it, takes all 
        children, parses each and writes a correcponding markdown file. If parser encounters some 
        errors, it returns them in a list. 
    */
    def literate(
          destBase: Option[File] = None
        , withIndex: Boolean = true
        ): List[String] = {

      /* First we can generate index section */
      val index = root getFileTree { f => f.isDirectory || f.isSource } match {
          case Some(ix) if withIndex => Seq("------", "### Index", ix) mkString "\n\n"
          case _ => ""
        }

      /* Then we start with traversing list of source files */
      val fileList = root getFileList { _.isSource }

      fileList flatMap { child =>

        /* And for each of them we generate a block of relative links */
        val linksList = fileList map { f =>
            "["+f.relativePath(root).toString+"]: "+f.relativePath(child).toString+".md"
          } mkString("\n")

        langMap.get(child.ext) flatMap { lang =>

          /* Knowing the language of the source we can parse it */
          val literator = LiteratorParsers(lang)
          val src = scala.io.Source.fromFile(child).mkString
          val parsed = literator.parseAll(literator.markdown, src) 

          parsed match {
            case literator.NoSuccess(msg, _) => Some(child + " " + parsed)
            case literator.Success(result, _) => {
              val text = Seq(result, index, linksList) mkString "\n\n"

              /* And if we parsed something, we write it to the file */
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

