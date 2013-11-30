Nice.scalaProject


// root project is only for aggregating:
publish := {}

generateDocs := {}

test := {}


// common settings:
homepage in ThisBuild := Some(url("https://github.com/laughedelic/literator"))

organization in ThisBuild := "laughedelic"


// subprojects:
lazy val lib = project
lazy val app = project dependsOn lib
lazy val plugin = project dependsOn lib
