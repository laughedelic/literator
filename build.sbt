Nice.scalaProject

name := "literator"
description := "An sbt plugin which converts sources to markdown"
organization := "laughedelic"

sbtPlugin := true
scalaVersion := "2.10.5"
publishBucketSuffix := "era7.com"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % Test

bintrayRepository in bintray := "sbt-plugins"
bintrayOrganization in bintray := None
// comment out this line to publish it to bintray
disablePlugins(BintrayPlugin)

// this is covered by the test
generateDocs := {}
