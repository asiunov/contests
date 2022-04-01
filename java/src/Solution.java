import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class Solution {
    private void solve() throws IOException {
        println(nextInt());
    }

    private static final boolean runNTestsInProd = true;
    private static final boolean printCaseNumber = false;
    private static final boolean assertInProd = false;

    private static final boolean logToFile = false;
    private static final boolean readFromConsoleInDebug = false;
    private static final boolean writeToConsoleInDebug = true;
    private static final boolean testTimer = false;

    private static Boolean isDebug = null;
    private BufferedReader in;
    private StringTokenizer line;
    private PrintWriter out;

    public static void main(String[] args) throws Exception {
        isDebug = Arrays.asList(args).contains("DEBUG_MODE");
        if (isDebug) {
            log = logToFile ? new PrintWriter("logs/log_" + System.currentTimeMillis() + ".txt") : new PrintWriter(System.out);
            clock = Clock.systemDefaultZone();
        }
        new Solution().run();
    }

    private void run() throws Exception {
        in = new BufferedReader(new InputStreamReader(!isDebug || readFromConsoleInDebug ? System.in : new FileInputStream("input.txt")));
        out = !isDebug || writeToConsoleInDebug ? new PrintWriter(System.out) : new PrintWriter("output.txt");

        try (Timer totalTimer = new Timer("total")) {
            int t = runNTestsInProd || isDebug ? nextInt() : 1;
            for (int i = 0; i < t; i++) {
                if (printCaseNumber) {
                    out.print("Case #" + (i + 1) + ": ");
                }
                if (testTimer) {
                    try (Timer testTimer = new Timer("test #" + (i + 1))) {
                        solve();
                    }
                } else {
                    solve();
                }
                if (isDebug) {
                    out.flush();
                }
            }
        }

        in.close();
        out.flush();
        out.close();
    }

    private void println(Object... objects) {
        boolean isFirst = true;
        for (Object o : objects) {
            if (!isFirst) {
                out.print(" ");
            } else {
                isFirst = false;
            }
            out.print(o.toString());
        }
        out.println();
    }

    private int[] nextIntArray(int n) throws IOException {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = nextInt();
        }
        return res;
    }

    private long[] nextLongArray(int n) throws IOException {
        long[] res = new long[n];
        for (int i = 0; i < n; i++) {
            res[i] = nextLong();
        }
        return res;
    }

    private int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }

    private long nextLong() throws IOException {
        return Long.parseLong(nextToken());
    }

    private double nextDouble() throws IOException {
        return Double.parseDouble(nextToken());
    }

    private char[] nextTokenChars() throws IOException {
        return nextToken().toCharArray();
    }

    private String nextToken() throws IOException {
        while (line == null || !line.hasMoreTokens()) {
            line = new StringTokenizer(in.readLine());
        }
        return line.nextToken();
    }

    private static void assertPredicate(boolean p) {
        if ((isDebug || assertInProd) && !p) {
            throw new RuntimeException();
        }
    }

    private static void assertPredicate(boolean p, String message) {
        if ((isDebug || assertInProd) && !p) {
            throw new RuntimeException(message);
        }
    }

    private static <T> void assertNotEqual(T unexpected, T actual) {
        if ((isDebug || assertInProd) && Objects.equals(actual, unexpected)) {
            throw new RuntimeException("assertNotEqual: " + unexpected + " == " + actual);
        }
    }

    private static void assertEqual(int expected, int actual) {
        if ((isDebug || assertInProd) && !Objects.equals(actual, expected)) {
            throw new RuntimeException("assertEqual: " + expected + " != " + actual);
        }
    }

    private static PrintWriter log = null;

    private static Clock clock = null;

    private static void log(Object... objects) {
        log(true, objects);
    }

    private static void logNoDelimiter(Object... objects) {
        log(false, objects);
    }

    private static void log(boolean printDelimiter, Object[] objects) {
        if (isDebug) {
            StringBuilder sb = new StringBuilder();
            sb.append(LocalDateTime.now(clock)).append(" - ");
            boolean isFirst = true;
            for (Object o : objects) {
                if (!isFirst && printDelimiter) {
                    sb.append(" ");
                } else {
                    isFirst = false;
                }
                sb.append(o.toString());
            }
            log.println(sb);
            log.flush();
        }
    }

    private static class Timer implements Closeable {
        private final String label;
        private final long startTime = isDebug ? System.nanoTime() : 0;

        public Timer(String label) {
            this.label = label;
        }

        @Override
        public void close() throws IOException {
            if (isDebug) {
                long executionTime = System.nanoTime() - startTime;
                String fraction = Long.toString(executionTime / 1000 % 1_000_000);
                logNoDelimiter("Timer[", label, "]: ", executionTime / 1_000_000_000, '.', "00000".substring(0, 6 - fraction.length()), fraction, 's');
            }
        }
    }

    private static <T> T timer(String label, Callable<T> f) throws Exception {
        if (isDebug) {
            try (Timer timer = new Timer(label)) {
                return f.call();
            }
        } else {
            return f.call();
        }
    }
}
