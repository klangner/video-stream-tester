package vst

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object MainApp {

  implicit val system: ActorSystem = ActorSystem("video-stream-tester")

  val TEST_HLS_STREAM = "http://184.72.239.149/vod/smil:BigBuckBunny.smil/playlist.m3u8"

  val route: Route = {
    path("/healthcheck") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "ok"))
      }
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    createMonitor(TEST_HLS_STREAM)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def createMonitor(streamUrl: String): Unit = {
    system.actorOf(StreamMonitorActor.props(streamUrl), "eu-central")
  }
}
