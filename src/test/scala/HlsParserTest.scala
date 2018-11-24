import org.scalatest.{FlatSpec, Matchers}
import vst.HlsParser
import vst.HlsParser.StreamInfo


class HlsParserTest extends FlatSpec with Matchers {

  val master: String =
    """
      |#EXTM3U
      |#EXT-X-STREAM-INF:BANDWIDTH=1296,RESOLUTION=640x360
      |640x360_1200.m3u8
      |#EXT-X-STREAM-INF:BANDWIDTH=264,RESOLUTION=416x234
      |416x234_200.m3u8
      |#EXT-X-STREAM-INF:BANDWIDTH=464,RESOLUTION=480x270
      |480x270_400.m3u8
      |#EXT-X-STREAM-INF:BANDWIDTH=1628,RESOLUTION=960x540
      |960x540_1500.m3u8
      |#EXT-X-STREAM-INF:BANDWIDTH=2628,RESOLUTION=1280x720
      |1280x720_2500.m3u8
    """.stripMargin

  "HlsParser" should "parse master playlist" in {
    val streamInfos = HlsParser.parseMasterPlaylist(master)
    val expected = Seq(
      StreamInfo(1296, "640x360_1200.m3u8"),
      StreamInfo(264, "416x234_200.m3u8"),
      StreamInfo(464, "480x270_400.m3u8"),
      StreamInfo(1628, "960x540_1500.m3u8"),
      StreamInfo(2628, "1280x720_2500.m3u8")
    )
    streamInfos shouldBe expected
  }

}