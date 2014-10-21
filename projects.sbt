// val era7Publish = Command.command("era7Publish") { st: State =>
//   val newSt = sbtrelease.ReleaseStateTransformations.reapply(
//     Classpaths.publishSettings ++ Seq(
//     publishMavenStyle := true,
//     publishBucketSuffix := "era7.com",
//     publishTo := Some(publishS3Resolver.value)
//   ), st)
//   val extracted = Project.extract(newSt)
//   val ref = extracted.get(thisProjectRef)
//   extracted.runAggregated(publish in ref, newSt)
// }

lazy val commonSettings: Seq[Setting[_]] =
  Nice.scalaProject ++
  // Literator.settings ++ 
  // bintrayPublishSettings ++
  Seq[Setting[_]](
    Literator.docsMap := Map(),
    //   val n = name.value.stripPrefix("literator-")
    //   Map(file(n+"/src/main/scala") -> file("docs/src/"+n))
    // },
    cleanFiles += file("docs/src/"), //Literator.docsOutputDirs.value,
    homepage := Some(url("https://github.com/laughedelic/literator")),
    organization := "laughedelic",
    // scalaVersion := "2.11.2",
    // commands += era7Publish,
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
