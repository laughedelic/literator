/* ### Command line application */

package laughedelic.literator.app

import java.io._
import laughedelic.literator.lib._
import laughedelic.literator.lib.FileUtils._

import org.rogach.scallop._
import buildinfo._


/* This is configuration class, defining command line options using Scallop */
case class AppConf(arguments: Seq[String]) extends ScallopConf(arguments) {

  version(s"""|literator_ ${BuildInfo.version} - generating docs from sources
              |(c) 2013 Alexey Alekhin (laughedelic)
              |""".stripMargin)

  /*  Using this option user can set the mapping between source input dirs and 
      docs output dirs. For example,
      `-M app/src=docs/src/app lib/src=docs/src/app`
      becomes
      `Map("app/src" -> "docs/src/app", "lib/src" -> "docs/src/app")`
  */
  val docsMap = props[String](
        name = 'M'
      , descr = "Map between sources and docs directories"
      , keyName = " input"
      , valueName = "output"
      )
  // TODO: validate existence of all files (+maybe warn if output dirs are not empty)
}


/* This is the class that the sbt-launcher will use */
class Main extends xsbti.AppMain {
  def run(config: xsbti.AppConfiguration) = new Exit(Main.exec(config.arguments))
}

case class Exit(val code: Int) extends xsbti.Exit


/* This object contains the actual code for running */
object Main {
  def exec(args: Array[String]): Int = {

    val conf = AppConf(args)

    if (conf.docsMap.isEmpty) conf.printHelp()

    /* Here we just iterate over the map, generate docs and output errors if any */
    conf.docsMap foreach { case (in, out) =>
      println("Generating documentation for " + in)

      val errors = file(in).literate(Some(file(out)))
      errors foreach { println }

      if (errors.nonEmpty) sys.error("Couldn't generate documentation due to parsing errors")
      else println("Documentation is written to  " + out)
    }

    0
  }

  /* For testing within sbt 'run <args>' will execute this app */
  def main(args: Array[String]): Unit = exec(args)
}
