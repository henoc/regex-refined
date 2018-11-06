name := "regex-refined"

version := "0.1"

ThisBuild / scalaVersion := "2.12.7"
run := (run in Compile in core).evaluated

lazy val macros = (project in file("macros")) settings (
  scalacOptions ++= Seq("-language:experimental.macros"),
  libraryDependencies += scalaOrganization.value % "scala-reflect" % scalaVersion.value
)

lazy val core = (project in file("core")) dependsOn macros settings (
  scalacOptions ++= Seq("-language:reflectiveCalls"),
  libraryDependencies += "eu.timepit" %% "refined" % "0.9.3"
)
