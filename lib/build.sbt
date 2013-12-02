Nice.scalaProject


name := "literator-lib"
  
description := "A simple library which converts sources to markdown"


docsInputDir := "lib/src/main/scala"

docsOutputDir := "docs/src/lib"


libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"


bintrayPublishSettings
