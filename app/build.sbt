Nice.scalaProject

// Nice.fatArtifactSettings


name := "literator-app"
  
description := "A simple command line application which converts sources to markdown"


docsInputDir := "app/src/main/scala"

docsOutputDir := "docs/src/app"

test <<= generateDocs


libraryDependencies ++= Seq (
  "org.rogach" %% "scallop" % "0.9.4"
// "org.scala-sbt" % "launcher-interface" % "0.12.1" % "provided"
)


// lint complains too much about scallop config stuff
scalacOptions ~= { opts => opts.filter(_ != "-Xlint") }


bintrayPublishSettings


buildInfoSettings

sourceGenerators in Compile <+= buildInfo


conscriptSettings
