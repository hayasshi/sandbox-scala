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
    libraryDependencies ++= Seq(
      scalaTest % Test
    )
  )

lazy val parser = (project in file("parser"))
  .settings(
    name := "parser",
    scalaVersion     := "2.12.8",
    version          := "0.1.0-SNAPSHOT",
    organization     := "com.github.hayasshi",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      fastparse,
    )
  )

lazy val akka26 = (project in file("akka-2.6"))
  .settings(
    name := "akka-2.6",
    scalaVersion     := "2.12.8",
    version          := "0.1.0-SNAPSHOT",
    organization     := "com.github.hayasshi",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.0",
      "com.typesafe.akka" %% "akka-stream-typed" % "2.6.0",
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.0" % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.0" % Test,
    )
  )
