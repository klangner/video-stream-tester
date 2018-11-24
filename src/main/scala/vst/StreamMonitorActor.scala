package vst

import java.net.URL

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString

import scala.util.{Failure, Success}


object StreamMonitorActor {

  def props(streamUrl: String): Props = Props(new StreamMonitorActor(streamUrl))

}


class StreamMonitorActor(masterUrl: String) extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  private val http = Http(context.system)
  private val baseUrl = masterUrl.substring(0, masterUrl.lastIndexOf('/')+1)

  override def preStart(): Unit = {
    loadMasterPlaylist(masterUrl)
  }

  /** Load playlist and start listening the stream with the highest bit stream rate */
  def loadMasterPlaylist(uri: String): Unit = {
    http.singleRequest(HttpRequest(uri = masterUrl))
      .onComplete {
        case Failure(_)   => log.warning(s"Can't load master playlist from $uri")
        case Success(res) =>
          res.entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
            val mediaUrl = highestBitrateStreamUrl(body.utf8String)
            loadMediaPlaylist(mediaUrl)
          }
      }
  }

  def highestBitrateStreamUrl(text: String): String = {
    println(text)
    HlsParser.parseMasterPlaylist(text)
      .maxBy(_.bandwidth)
      .playlistUrl
  }

  def loadMediaPlaylist(mediaUrl: String): Unit = {
    http.singleRequest(HttpRequest(uri = baseUrl + mediaUrl))
      .pipeTo(self)
  }

  def receive: PartialFunction[Any, Unit] = {

    case HttpResponse(StatusCodes.OK, _, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        println("Media playlist contents: \n" + body.utf8String)
      }

    case resp @ HttpResponse(code, _, _, _) =>
      log.warning(s"Request failed, response code: $code")
      resp.discardEntityBytes()
  }
}