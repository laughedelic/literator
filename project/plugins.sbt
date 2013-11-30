resolvers += "Era7 Releases" at "http://releases.era7.com.s3.amazonaws.com"

addSbtPlugin("ohnosequences" % "nice-sbt-settings" % "0.3.1")


resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
    url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
        Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.1")


// for app:
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.0")

addSbtPlugin("net.databinder" % "conscript-plugin" % "0.3.5")
