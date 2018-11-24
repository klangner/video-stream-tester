name := "video-stream-tester"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= {

  val akkaActorVersion = "2.5.18"
  val akkaHttpVersion = "10.1.5"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaActorVersion
//    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  )
}