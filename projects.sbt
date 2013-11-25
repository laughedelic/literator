lazy val literator = project in file(".") dependsOn(lib) aggregate(lib, app, plugin)

homepage in ThisBuild := Some(url("https://github.com/laughedelic/literator"))

organization in ThisBuild := "ohnosequences"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"

// subprojects:
lazy val lib = project
lazy val app = project dependsOn lib
lazy val plugin = project dependsOn lib
