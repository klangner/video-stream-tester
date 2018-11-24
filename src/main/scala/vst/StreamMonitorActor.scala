package vst

import akka.actor.{Actor, Props}
import vst.StreamMonitorActor._


object StreamMonitorActor {

  def props(streamUrl: String): Props = Props(new StreamMonitorActor(streamUrl))

  case object Start
  case object Stop

}


class StreamMonitorActor(streamURL: String) extends Actor {

  override def receive: Receive = {

    case Start =>
      println("Start monitoring")

    case Stop =>
      println("Stop monitoring, kill actor")
  }
}