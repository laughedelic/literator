package ohnosequences.literator.plugin

import sbt._
import Keys._
import ohnosequences.literator.lib._

object LiteratorPlugin extends sbt.Plugin {

  // Setting keys:
  lazy val docsInputDir = settingKey[String]("Directory with the documented sources")
  lazy val docsOutputDir = settingKey[String]("Output directory for the generated documentation")
  lazy val generateDocs = taskKey[Unit]("Generates markdown docs from code using literator tool")

  lazy val literatorSettings: Seq[Setting[_]] = Seq(
    docsInputDir := sourceDirectory.value.toString
  , docsOutputDir := "docs/src/"
  , generateDocs := {
      val s: TaskStreams = streams.value
      s.log.info("Generating documentation...")

      val errors = new File(docsInputDir.value).literate(Some(new File(docsOutputDir.value)))
      errors foreach { s.log.error(_) }

      if (errors.nonEmpty) sys.error("Couldn't generate documantation due to parsing errors")
      else s.log.info("Documentation is written to " + docsOutputDir.value)
    }
  )

}
