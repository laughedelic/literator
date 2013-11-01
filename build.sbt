import ohnosequences.sbt._

Era7.allSettings

name := "literator"

description := "A simple tool which converts source code to a markdown document"

homepage := Some(url("https://github.com/laughedelic/literator"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses := Seq("AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt"))


bucketSuffix := "era7.com"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0.+" % "test"


scalaVersion := "2.10.3"

scalacOptions ++= Seq(
    "-feature"
  , "-language:higherKinds"
  , "-language:implicitConversions"
  , "-language:postfixOps"
  , "-deprecation"
  , "-unchecked"
  )

com.github.retronym.SbtOneJar.oneJarSettings
