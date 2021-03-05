import java.nio.charset.CodingErrorAction
import scala.io.Codec
//import ReadingFiles.and_text

object DownloadFiles extends App {
  val url = "https://www.gutenberg.org/files/13177/13177-8.txt"

  // Handling MalformedInputException
  implicit val codec = Codec("ISO-8859-1")
  codec.onMalformedInput(CodingErrorAction.REPLACE)
  codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

  import scala.io.Source
  val txtBuffer = Source.fromURL(url) //so network resources is used here, could take a while for request for big file

  //we could have goten just a string without splitting lines by
  //val s = txtBuffer.mkString

  val lines = txtBuffer.getLines.toArray //so we will exhaust our buffer here
  println(txtBuffer.size) //notice this was 0 after we called getLines


  //lets see what the early lines show
  lines.slice(0,20).foreach(println)
  val txt = lines.mkString("\n")

  val relative_save_path = "src/resources/cookbook.txt"

  Utilities.saveLines(lines, relative_save_path) //let's try with default "\n" separator
}