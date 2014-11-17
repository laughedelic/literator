lazy val commonSettings: Seq[Setting[_]] =
  Nice.scalaProject ++
  // Comment our this line to publish to Era7 repository:
  // bintrayPublishSettings ++
  Seq[Setting[_]](
    Literator.docsMap := Map(),
    cleanFiles += file("docs/src/"), //Literator.docsOutputDirs.value,
    homepage := Some(url("https://github.com/laughedelic/literator")),
    organization := "laughedelic",
    scalaVersion := "2.10.4",
    publishBucketSuffix := "era7.com",
    GithubRelease.assets := Seq()
  )

// subprojects:
lazy val lib = project settings(commonSettings: _*)
lazy val app = project settings(commonSettings: _*) dependsOn lib
lazy val plugin = project settings(commonSettings: _*) dependsOn lib

// root project is only for aggregating:
commonSettings

publish := {}

Literator.docsMap := Map()
