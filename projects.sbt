lazy val commonSettings: Seq[Setting[_]] =
  Nice.scalaProject ++
  Literator.settings ++ 
  bintrayPublishSettings ++
  Seq[Setting[_]](
    Literator.docsMap := {
      val n = name.value.stripPrefix("literator-")
      Map(file(n+"/src/main/scala") -> file("docs/src/"+n))
    },
    cleanFiles ++= Literator.docsOutputDirs.value,
    homepage := Some(url("https://github.com/laughedelic/literator")),
    organization := "laughedelic"
  )

commonSettings

// subprojects:
lazy val lib = project settings(commonSettings: _*)
lazy val app = project settings(commonSettings: _*) dependsOn lib
lazy val plugin = project settings(commonSettings: _*) dependsOn lib

// root project is only for aggregating:
publish := {}

Literator.docsMap := Map()
