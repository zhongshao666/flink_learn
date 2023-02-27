import org.apache.commons.io.output.ByteArrayOutputStream
import org.lionsoul.ip2region.xdb.Searcher

import java.io.InputStream

object TestIp {

  def main(args: Array[String]): Unit = {
    val ra: InputStream = TestIp.getClass.getResourceAsStream("/ip2region.xdb")
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val buffer: Array[Byte] = new Array[Byte](4096)
    Stream.continually(ra.read(buffer)).takeWhile(_ != -1).foreach(baos.write(buffer, 0, _))
    val searcher: Searcher = Searcher.newWithBuffer(baos.toByteArray)
//    new Searcher(null,null,baos.toByteArray)

    val str: String = searcher.search("101.32.42.101")
    println(str)
  }

}
