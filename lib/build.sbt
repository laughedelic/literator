name := "literator-lib"
description := "A simple library which converts sources to markdown"

// crossScalaVersions := Seq("2.10.4", "2.11.0")

// libraryDependencies ++= {
//   // if scala 2.11+ is used, parser combinators are a separate package
//   CrossVersion.partialVersion(scalaVersion.value) match {
//     case Some((2, scalaMajor)) if scalaMajor >= 11 =>
//       Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1")
//     case _ => Seq()
//   }
// }

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % Test
