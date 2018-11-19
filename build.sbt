ThisBuild / name := "regex-refined"

ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.12.7"

ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.7")

ThisBuild / libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-language:_"
)


lazy val root = (project in file(".")) dependsOn core settings (
  scalacOptions ++= Seq(
    // "-Ymacro-debug-lite"
  )
)

lazy val core = (project in file("core")) settings (
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "eu.timepit" %% "refined" % "0.9.3",
    "com.github.mpilquist" %% "simulacrum" % "0.14.0"
  )
)
