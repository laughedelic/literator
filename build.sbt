name         := "literator"
description  := "An sbt plugin which converts sources to markdown"
organization := "laughedelic"

sbtPlugin := true
sbtVersion := "1.0.2"
scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

conflictManager := ConflictManager.default

bintrayReleaseOnPublish := !isSnapshot.value
bintrayPackageLabels := Seq("sbt", "sbt-plugin", "documentation", "literate-programming")

publishMavenStyle := false
publishTo := (publishTo in bintray).value
