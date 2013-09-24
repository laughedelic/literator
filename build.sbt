import sbtrelease._

name := "literator"

description := "A simple tool which converts source code to a markdown document"

homepage := Some(url("https://github.com/laughedelic/literator"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses := Seq("AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt"))


scalaVersion := "2.10.2"


publishMavenStyle := true

publishTo <<= (isSnapshot, s3credentials) {
                (snapshot,   credentials) =>
  val prefix = if (snapshot) "snapshots" else "releases"
  credentials map S3Resolver(
      "Era7 "+prefix+" S3 bucket"
    , "s3://"+prefix+".era7.com"
    , Resolver.mavenStylePatterns
    ).toSbtResolver
}


libraryDependencies += "org.scalatest" %% "scalatest" % "2.0.+" % "test"


scalacOptions ++= Seq(
    "-feature"
  , "-language:higherKinds"
  , "-language:implicitConversions"
  , "-language:postfixOps"
  , "-deprecation"
  , "-unchecked"
  )


releaseSettings

com.github.retronym.SbtOneJar.oneJarSettings
