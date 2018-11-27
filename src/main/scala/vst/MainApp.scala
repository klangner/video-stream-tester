package vst

import scala.io.Source

object MainApp {

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      printUsage()
    } else {
      monitorStream(args(0))
    }
  }


  def printUsage(): Unit = {
    println("Usage:")
    println("  video-stream-tester <stream-url>")
  }


  def monitorStream(streamUrl: String): Unit = {
    val rootPath = streamUrl.substring(0, streamUrl.lastIndexOf('/') + 1)
    val masterData = Source.fromURL(streamUrl).mkString
    val masterPlaylist = HlsParser.parseMasterPlaylist(masterData)

    println(s"Monitor stream $streamUrl")
    println(s"Root path $rootPath")
    println(masterData)
    println(masterPlaylist)
  }

}
