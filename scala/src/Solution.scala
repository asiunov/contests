import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.time.Clock
import java.time.LocalDateTime
import java.util
import java.util.Arrays
import java.util.Objects
import java.util.StringTokenizer

// VM options: -Xmx2048m -Xss1024m
object Solution {
  private val runNTestsInProd = true

  class Runner() {
    private[this] def solve(): Unit = {
      println(nextInt())
    }


    private var in: BufferedReader = _
    private var line: StringTokenizer = _
    private var out: PrintWriter = _

    def run(): Unit = {
      in = new BufferedReader(new InputStreamReader(if (!isDebug || readFromConsoleInDebug) System.in else new FileInputStream("input.txt")))
      out = if (!isDebug || writeToConsoleInDebug) new PrintWriter(System.out) else new PrintWriter("output.txt")
      timer("total") {
        val t = if (runNTestsInProd || isDebug) nextInt() else 1
        for (i <- 0 until t) {
          if (printCaseNumber) {
            out.print("Case #" + (i + 1) + ": ")
          }
          if (testTimer) {
            timer("test #" + (i + 1)) {
              solve()
            }
          } else {
            solve()
          }
          if (isDebug) {
            out.flush()
          }
        }
      }
      in.close()
      out.flush()
      out.close()
    }

    private def println(objects: Any*): Unit = out.println(objects.mkString(" "))

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

    private def nextTokenChars(): Array[Char] = nextToken().toCharArray

    private def nextToken(): String = {
      while (line == null || !line.hasMoreTokens) {
        line = new StringTokenizer(in.readLine)
      }
      line.nextToken()
    }
  }

  private def assertPredicate(p: Boolean, message: String = ""): Unit =
    if ((isDebug || assertInProd) && !p) {
      throw new RuntimeException(message)
    }

  private def assertNotEqual[T](unexpected: T, actual: T): Unit =
    if ((isDebug || assertInProd) && actual == unexpected) {
      throw new RuntimeException("assertNotEqual: " + unexpected + " == " + actual)
    }

  private def assertEqual(expected: Int, actual: Int): Unit =
    if ((isDebug || assertInProd) && actual != expected) {
      throw new RuntimeException("assertEqual: " + expected + " != " + actual)
    }

  private val printCaseNumber = false
  private val assertInProd = false

  private val logToFile = false
  private val readFromConsoleInDebug = false
  private val writeToConsoleInDebug = true
  private val testTimer = false

  private var isDebug: java.lang.Boolean = _
  private var log: PrintWriter = _
  private var clock: Clock = _

  private def log(objects: Any*): Unit = _log(printDelimiter = true, objects)

  private def logNoDelimiter(objects: Any*): Unit = _log(printDelimiter = false, objects)

  private def _log(printDelimiter: Boolean, objects: Seq[Any]): Unit =
    if (isDebug) {
      log.println(s"${LocalDateTime.now(clock)} - ${if (printDelimiter) objects.mkString(" ") else objects.mkString}")
      log.flush()
    }

  def timer[T](label: String)(f: => T): T =
    if (isDebug) {
      val startTime = System.nanoTime
      try {
        f
      } finally {
        logNoDelimiter(s"Timer[$label]: ${(System.nanoTime - startTime) % .6f}s")
      }
    } else {
      f
    }

  def main(args: Array[String]): Unit = {
    isDebug = args.contains("DEBUG_MODE")
    if (isDebug) {
      log = if (logToFile) new PrintWriter("logs/log_" + System.currentTimeMillis + ".txt") else new PrintWriter(System.out)
      clock = Clock.systemDefaultZone
    }
    new Runner().run()
  }
}
