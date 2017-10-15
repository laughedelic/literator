name         := "literator"
description  := "An sbt plugin which converts sources to markdown"
organization := "laughedelic"

sbtPlugin := true
sbtVersion := "0.13.16"
scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

bintrayReleaseOnPublish := !isSnapshot.value
bintrayPackageLabels := Seq("sbt", "sbt-plugin", "documentation", "literate-programming")

publishMavenStyle := false
publishTo := (publishTo in bintray).value
