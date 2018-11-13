ThisBuild / name := "regex-refined"

ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.12.7"

ThisBuild / libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

ThisBuild / scalacOptions ++= Seq("-deprecation", "-feature")


lazy val root = (project in file(".")) dependsOn user

lazy val macros = (project in file("macros")) settings (
  scalacOptions ++= Seq("-language:experimental.macros"),
  libraryDependencies ++= Seq(
    scalaOrganization.value % "scala-reflect" % scalaVersion.value,
    scalaOrganization.value % "scala-compiler" % scalaVersion.value
  )
)

lazy val core = (project in file("core")) dependsOn macros settings (
  scalacOptions ++= Seq("-language:reflectiveCalls", "-language:implicitConversions"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "eu.timepit" %% "refined" % "0.9.3",
    "com.github.mpilquist" %% "simulacrum" % "0.14.0"
  )
)

lazy val user = (project in file("user")) dependsOn core
