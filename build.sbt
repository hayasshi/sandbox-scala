import Dependencies._

val ScalaVersion = "2.13.7"
val Organization = "com.github.hayasshi"

lazy val root = (project in file("."))
  .settings(
    name         := "sandbox-scala",
    organization := Organization,
    scalaVersion := ScalaVersion
  )
  .aggregate(
    algorithm,
    parser,
    akka26,
    actor
  )

lazy val algorithm = (project in file("algorithm"))
  .settings(
    name         := "algorithm",
    organization := Organization,
    scalaVersion := ScalaVersion,
    version      := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      scalaTest % Test
    )
  )

lazy val parser = (project in file("parser"))
  .settings(
    name         := "parser",
    organization := Organization,
    scalaVersion := ScalaVersion,
    version      := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      fastparse
    )
  )

lazy val akka26 = (project in file("akka-2.6"))
  .settings(
    name         := "akka-2.6",
    organization := Organization,
    scalaVersion := ScalaVersion,
    version      := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      scalaTest            % Test,
      "com.typesafe.akka" %% "akka-actor-typed"         % "2.6.0",
      "com.typesafe.akka" %% "akka-stream-typed"        % "2.6.0",
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.0" % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"      % "2.6.0" % Test
    )
  )

lazy val actor = (project in file("actor"))
  .settings(
    name         := "akka-2.6",
    organization := Organization,
    scalaVersion := ScalaVersion,
    version      := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      scalaTest                % Test,
      "com.typesafe.akka"     %% "akka-actor"   % "2.6.17",
      "com.typesafe.akka"     %% "akka-testkit" % "2.6.17" % Test,
      "software.amazon.awssdk" % "sqs"          % "2.17.81"
    )
  )
