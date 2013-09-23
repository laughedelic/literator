import sbtrelease._

name := "code-to-markdown"

description := "code-to-markdown project"

homepage := Some(url("https://github.com/laughedelic/code-to-markdown"))

// organization := "ohnosequences"

// organizationHomepage := Some(url("http://ohnosequences.com"))

licenses := Seq("AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt"))


scalaVersion := "2.10.2"


publishMavenStyle := true

// for publishing you need to set `s3credentials`
// publishTo <<= (isSnapshot, s3credentials) { 
//                 (snapshot,   credentials) => 
//   val prefix = if (snapshot) "snapshots" else "releases"
//   credentials map S3Resolver(
//       "Era7 "+prefix+" S3 bucket"
//     , "s3://"+prefix+".era7.com"
//     , Resolver.mavenStylePatterns
//     ).toSbtResolver
// }


resolvers ++= Seq ( 
    "Era7 Releases"  at "http://releases.era7.com.s3.amazonaws.com"
//, "Era7 Snapshots" at "http://snapshots.era7.com.s3.amazonaws.com"
  , Resolver.url("Era7 ivy releases", url("http://releases.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)
//, Resolver.url("Era7 ivy snapshots", url("http://snapshots.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)
  )


scalacOptions ++= Seq(
    "-feature"
  , "-language:higherKinds"
  , "-language:implicitConversions"
  , "-language:postfixOps"
  , "-deprecation"
  , "-unchecked"
  )


// sbt-release settings
releaseSettings
