import bintray.Keys._

Nice.scalaProject

sbtPlugin := true


name := "literator-plugin"
  
description := "An sbt plugin which converts sources to markdown"


docsInputDir := "plugin/src/main/scala"

docsOutputDir := "docs/src/plugin"

test <<= generateDocs


bintrayPublishSettings

repository in bintray := "sbt-plugins"

bintrayOrganization in bintray := None
