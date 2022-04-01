import java.io.BufferedReader
import java.io.FileInputStream
import java.io.PrintWriter
import java.time.Clock
import java.time.LocalDateTime
import java.util.Arrays
import java.util.Objects
import java.util.StringTokenizer

// VM options: -Xmx2048m -Xss1024m
class Solution {
    private val runNTestsInProd = true

    private fun solve() {
        println(nextInt())
    }

    private var `in`: BufferedReader? = null
    private var line: StringTokenizer? = null
    private var out: PrintWriter? = null

    private fun run() {
        `in` = (if (!isDebug!! || readFromConsoleInDebug) System.`in` else FileInputStream("input.txt")).bufferedReader()
        out = if (!isDebug!! || writeToConsoleInDebug) PrintWriter(System.out) else PrintWriter("output.txt")
        timer("total") {
            val t = if (runNTestsInProd || isDebug!!) nextInt() else 1
            for (i in 0 until t) {
                if (printCaseNumber) {
                    out!!.print("Case #${i + 1}: ")
                }
                if (testTimer) {
                    timer("test #${i + 1}") {
                        solve()
                    }
                } else {
                    solve()
                }
                if (isDebug!!) {
                    out!!.flush()
                }
            }
        }
        `in`!!.close()
        out!!.flush()
        out!!.close()
    }

    private fun println(vararg objects: Any) = out!!.println(objects.joinToString(separator = " ") { x -> x.toString() })

    private fun nextIntArray(n: Int): IntArray {
        val res = IntArray(n)
        for (i in 0 until n) {
            res[i] = nextInt()
        }
        return res
    }

    private fun nextLongArray(n: Int): LongArray {
        val res = LongArray(n)
        for (i in 0 until n) {
            res[i] = nextLong()
        }
        return res
    }

    private fun nextInt(): Int = nextToken().toInt()

    private fun nextLong(): Long = nextToken().toLong()

    private fun nextDouble(): Double = nextToken().toDouble()

    private fun nextTokenChars(): CharArray = nextToken().toCharArray()

    private fun nextToken(): String {
        while (line == null || !line!!.hasMoreTokens()) {
            line = StringTokenizer(`in`!!.readLine())
        }
        return line!!.nextToken()
    }

    companion object {
        private const val printCaseNumber = false
        private const val assertInProd = false

        private const val logToFile = false
        private const val readFromConsoleInDebug = false
        private const val writeToConsoleInDebug = true
        private const val testTimer = false

        private var isDebug: Boolean? = null

        private fun assertPredicate(p: Boolean, message: String? = null) {
            if ((isDebug!! || assertInProd) && !p) {
                throw RuntimeException(message)
            }
        }

        private fun <T> assertNotEqual(unexpected: T, actual: T) {
            if ((isDebug!! || assertInProd) && actual == unexpected) {
                throw RuntimeException("assertNotEqual: $unexpected == $actual")
            }
        }

        private fun assertEqual(expected: Int, actual: Int) {
            if ((isDebug!! || assertInProd) && actual != expected) {
                throw RuntimeException("assertEqual: $expected != $actual")
            }
        }

        private var log: PrintWriter? = null
        private var clock: Clock? = null

        private fun log(vararg objects: Any) = log(delimiter = " ", objects)

        private fun logNoDelimiter(vararg objects: Any) = log(delimiter = "", objects)

        private fun log(delimiter: String, objects: Array<out Any>) {
            if (isDebug!!) {
                log!!.println(objects.joinToString(separator = delimiter, prefix = "${LocalDateTime.now(clock)} - ") { x -> x.toString() })
                log!!.flush()
            }
        }

        private fun <T> timer(label: String, f: () -> T): T {
            return if (isDebug!!) {
                val startTime = System.nanoTime()
                try {
                    f()
                } finally {
                    logNoDelimiter("Timer[%s]: %.6fs".format(label, (System.nanoTime() - startTime) / 1e9))
                }
            } else {
                f()
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            isDebug = args.contains("DEBUG_MODE")
            if (isDebug!!) {
                log = if (logToFile) PrintWriter("logs/log_${System.currentTimeMillis()}.txt") else PrintWriter(System.out)
                clock = Clock.systemDefaultZone()
            }
            Solution().run()
        }
    }
}

fun main(args: Array<String>) = Solution.main(args)
