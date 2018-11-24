package vst

import scala.collection.mutable.ListBuffer

object HlsParser {

  case class StreamInfo(bandwidth: Int, playlistUrl: String)

  private val bandwidthPattern = """BANDWIDTH=(\d*)""".r

  def parseMasterPlaylist(text: String): Seq[StreamInfo] = {
    val streams = new ListBuffer[StreamInfo]()
    var bandwidth = 0

    text.lines.foreach{ line =>
      if (line.startsWith("#EXT-X-STREAM-INF")) {
        bandwidthPattern
          .findFirstMatchIn(line)
          .map(_.group(1))
          .foreach(b => bandwidth = b.toInt)
      } else if (line.endsWith(".m3u8")) {
        streams.append(StreamInfo(bandwidth, line))
      }
    }
    streams.toList
  }
}
