resolvers ++= Seq(
  "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com",
  "Era7 maven snapshots" at "https://s3-eu-west-1.amazonaws.com/snapshots.era7.com"
)

addSbtPlugin("ohnosequences" % "nice-sbt-settings" % "0.6.0-SNAPSHOT")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.2.0")


// for app:
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2")

addSbtPlugin("net.databinder" % "conscript-plugin" % "0.3.5")

// itself for testing:
addSbtPlugin("laughedelic" % "literator-plugin" % "0.7.0-SNAPSHOT")
