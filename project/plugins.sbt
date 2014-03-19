resolvers ++= Seq(
  "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com",
  "Era7 maven snapshots" at "http://snapshots.era7.com.s3.amazonaws.com",
  "sbt-taglist-releases" at "http://johanandren.github.com/releases/",
  "laughedelic maven releases" at "http://dl.bintray.com/laughedelic/maven",
  Resolver.url("laughedelic sbt-plugins", url("http://dl.bintray.com/laughedelic/sbt-plugins"))(Resolver.ivyStylePatterns),
  Resolver.url("bintray-sbt-plugin-releases", url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("ohnosequences" % "nice-sbt-settings" % "0.4.0-RC2")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.1")


// for app:
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.0")

addSbtPlugin("net.databinder" % "conscript-plugin" % "0.3.5")

// itself for testing:
// addSbtPlugin("laughedelic" % "literator-plugin" % "0.5.1-SNAPSHOT")
