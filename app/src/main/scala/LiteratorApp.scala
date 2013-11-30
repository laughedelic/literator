/*
### Command line interface

* Input: 
  + a directory with sources or a single file
  + (optional) destination: path prefix or a file name respectively
* Output:
  + if second argument is given, file(s) with documentation
  + if not, it's set to `docs/` or `stdout` respectively
*/

package laughedelic.literator.app

import java.io._
import laughedelic.literator.lib._
import laughedelic.literator.lib.FileUtils._

import org.rogach.scallop._
import buildinfo._


// this is your conf class, see the Scallop docs for how to use this
case class AppConf(arguments: Seq[String]) extends ScallopConf(arguments) {

  version(s"literator ${BuildInfo.version} - generating docs from sources \n(c) 2013 Alexey Alekhin (laughedelic)\n")

  val docsMap = props[String](
        descr = "Map between sources and docs output directories"
      , keyName = "input-source"
      , valueName = "output-docs"
      )
}


// this is the class that the sbt-launcher will use
class Main extends xsbti.AppMain {
  def run(config: xsbti.AppConfiguration) = new Exit(Main.exec(config.arguments))
}

case class Exit(val code: Int) extends xsbti.Exit

object Main {
  // this is the actual code for running
  def exec(args: Array[String]): Int = {
    // TODO: use parsed opts
    val conf = AppConf(args)

    // if (args.length < 1) sys.error("Need at least one argument")
    // else file(args(0)).literate(if (args.length > 1) Some(file(args(1))) else None)

    // success!
    0
  }

  // for testing within sbt
  // 'run <args>' will execute your cmdline app
  def main(args: Array[String]): Unit = exec(args)
}
