name := "literator-app"
description := "A simple command line application which converts sources to markdown"

libraryDependencies += "org.rogach" %% "scallop" % "0.9.5"

// lint complains too much about scallop config stuff
// scalacOptions ~= { opts => opts.filter(_ != "-Xlint") }

buildInfoSettings
sourceGenerators in Compile <+= buildInfo

conscriptSettings
