import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.hayasshi"

lazy val root = (project in file("."))
  .settings(name := "sandbox-scala")
  .aggregate(
    algorithm,
    parser
  )

lazy val algorithm = (project in file("algorithm"))
  .settings(
    name := "algorithm",
    scalaVersion     := "2.12.8",
    version          := "0.1.0-SNAPSHOT",
    organization     := "com.github.hayasshi",
    libraryDependencies += scalaTest % Test
  )

lazy val parser = (project in file("parser"))
  .settings(
    name := "parser",
    scalaVersion     := "2.12.8",
    version          := "0.1.0-SNAPSHOT",
    organization     := "com.github.hayasshi",
    libraryDependencies += scalaTest % Test
  )
