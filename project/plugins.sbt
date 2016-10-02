resolvers ++= Seq(
  "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com",
  "Era7 maven snapshots" at "https://s3-eu-west-1.amazonaws.com/snapshots.era7.com"
)

addSbtPlugin("ohnosequences" % "nice-sbt-settings" % "0.6.0")
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")


// itself for testing:
// addSbtPlugin("laughedelic" % "literator-plugin" % "0.7.0-SNAPSHOT")
