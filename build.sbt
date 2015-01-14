name := "cryptopals"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

scalacOptions ++= Seq(
  "-encoding", "UTF-8", "-optimise",
  "-deprecation", "-unchecked", "-feature", "-Xlint",
  "-Ywarn-infer-any", "-Ywarn-value-discard")

scalacOptions in Test ++= Seq("-Yrangepos", "-Yinline-warnings")

javacOptions  ++= Seq(
  "-Xlint:unchecked", "-Xlint:deprecation") // Java 8: "-Xdiags:verbose")

// Enable improved incremental compilation feature in 2.11.X.
// see http://www.scala-lang.org/news/2.11.1
incOptions := incOptions.value.withNameHashing(true)
