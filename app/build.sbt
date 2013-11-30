Nice.scalaProject

Nice.fatArtifactSettings


name := "literator-app"
  
description := "A simple command line application which converts sources to markdown"


docsInputDir := "app/src/main/scala"

docsOutputDir := "docs/src/app"

test <<= generateDocs


bintrayPublishSettings
