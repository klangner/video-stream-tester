package vst

import scala.collection.mutable.ListBuffer

object HlsParser {

  case class MasterPlaylist(streams: Seq[StreamInfo])
  case class StreamInfo(bandwidth: Int, playlistUrl: String)


  private val BANDWIDTH_PATTERN = """BANDWIDTH=(\d*)""".r

  def parseMasterPlaylist(text: String): MasterPlaylist = {
    val streams = new ListBuffer[StreamInfo]()
    var bandwidth = 0

    text.lines.foreach{ line =>
      if (line.startsWith("#EXT-X-STREAM-INF")) {
        BANDWIDTH_PATTERN
          .findFirstMatchIn(line)
          .map(_.group(1))
          .foreach(b => bandwidth = b.toInt)
      } else if (line.endsWith(".m3u8")) {
        streams.append(StreamInfo(bandwidth, line))
      }
    }
    MasterPlaylist(streams.toList)
  }
}
