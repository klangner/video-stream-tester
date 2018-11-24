package vst

import akka.actor.ActorSystem

import scala.io.StdIn

object MainApp {

  val system = ActorSystem("VideoStreamTester")


  def main(args: Array[String]): Unit = {
    val streamMonitor = system.actorOf(StreamMonitorActor.props("localhost"), "eu-north")
    streamMonitor ! "Scandinavia"

    println(">>> Press ENTER to exit <<<")
    try StdIn.readLine()
    finally system.terminate()
  }
}
