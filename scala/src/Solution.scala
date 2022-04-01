import java.io.{BufferedReader, FileInputStream, InputStreamReader, PrintWriter}
import java.util.StringTokenizer

object Solution {
  private val inputFilename = "input.txt"
  private val outputFilename = "output.txt"

  def main(args: Array[String]): Unit = {
    Runner(args.contains("DEBUG_MODE")).run()
  }

  case class Runner(isDebug: Boolean) {
    private[this] def solve(): Unit = {
      println(nextInt())
    }


    private var in: BufferedReader = _
    private var line: StringTokenizer = _
    private var out: PrintWriter = _

    def run(): Unit = {
      if (isDebug) {
        in = new BufferedReader(new InputStreamReader(new FileInputStream(Solution.inputFilename)))
        // in = new BufferedReader(new InputStreamReader(System.in))
      }
      else {
        in = new BufferedReader(new InputStreamReader(System.in))
      }
      out = new PrintWriter(System.out)
      //      out = new PrintWriter(outputFilename)
      // val t = if (isDebug) nextInt() else 1
      //      val t = 1
      val t = nextInt()
      for (i <- 1 to t) {
        //        out.print(s"Case #$i: ")
        solve()
        out.flush()
      }
      in.close()
      out.flush()
      out.close()
    }

    private def println(objects: Any*): Unit = {
      for (o <- objects) {
        out.print(o.toString + " ")
      }
      out.println()
    }

    private def nextIntArray(n: Int): Array[Int] = {
      val res = Array.ofDim[Int](n)
      for (i <- res.indices) {
        res(i) = nextInt()
      }
      res
    }

    private def nextLongArray(n: Int): Array[Long] = {
      val res = Array.ofDim[Long](n)
      for (i <- res.indices) {
        res(i) = nextLong()
      }
      res
    }

    private def nextInt(): Int = nextToken().toInt

    private def nextLong(): Long = nextToken().toLong

    private def nextDouble(): Double = nextToken().toDouble

    private def nextToken(): String = {
      while ( {
        line == null || !line.hasMoreTokens
      }) line = new StringTokenizer(in.readLine)
      line.nextToken()
    }
  }
}
