lazy val commonSettings: Seq[Setting[_]] =
  Nice.scalaProject ++
  // Comment out this line to publish to Era7 repository:
  Seq[Setting[_]](
    // docsMap := Map(),
    // cleanFiles += file("docs/src/"), //Literator.docsOutputDirs.value,
    homepage := Some(url("https://github.com/laughedelic/literator")),
    organization := "laughedelic",
    scalaVersion := "2.10.5",
    publishBucketSuffix := "era7.com",
    GithubRelease.releaseAssets := Seq()
  )

// subprojects:
lazy val lib = project settings(commonSettings: _*) disablePlugins(BintrayPlugin)
lazy val plugin = project settings(commonSettings: _*) dependsOn lib disablePlugins(BintrayPlugin)

// root project is only for aggregating:
commonSettings
publish := {}
docsMap := Map()
generateDocs := {}
