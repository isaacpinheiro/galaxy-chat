name := "galaxy-chat"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % Test
)

libraryDependencies +=
  "com.typesafe.akka" %% "akka-remote" % "2.5.3"
