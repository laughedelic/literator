Nice.scalaProject


name := "literator-lib"
  
description := "A simple library which converts sources to markdown"


// docsInputDir := "lib/src/main/scala"

// docsOutputDir := "wiki/src/lib"

generateDocs := {}


libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"


bintrayPublishSettings
