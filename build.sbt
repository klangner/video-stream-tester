name := "video-stream-tester"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= {

  val akkaActorVersion = "2.5.18"
  val akkaStream = "2.5.12"
  val akkaHttpVersion = "10.1.5"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaActorVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaStream,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    // Test
    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )
}