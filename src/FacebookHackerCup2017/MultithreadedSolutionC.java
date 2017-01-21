package FacebookHackerCup2017;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class MultithreadedSolutionC {
    // to clean all blocks replace:
    // \/\/ begin clean[\s\S]*?\n([\t ]*)\/\/ end clean
    // \/\/ begin clean\n\n\$1/\/ end clean

    private static class Result {
        private int testNum;

        private Result(int testNum) {
            this.testNum = testNum;
        }

        // todo add fields to print result of one test =================================================================
        // begin clean
        private long res;
        // end clean
    }

    private void printResult(Result result) throws IOException {
        out.print("Case #" + result.testNum + ": ");

        // todo print result of one test ===============================================================================
        // begin clean
        out.println(result.res);
        // end clean
    }

    // begin clean

    // end clean


    private class Solver implements Callable<Result> {
        // todo add fields for input parameters ========================================================================
        // begin clean
        private int n, m;
        private int w1, aw, bw;
        private int d1, ad, bd;
        private long s1, as, bs;
        private int z1;
        private static final int mm = 1000000007;

        private long mult(long a, long b) {
            return a * b % mm;
        }
        // end clean

        private void readData() throws IOException {
            // todo read input parameters ==============================================================================
            // begin clean
            n = nextInt();
            m = nextInt();
            w1 = nextInt();
            aw = nextInt();
            bw = nextInt();
            d1 = nextInt();
            ad = nextInt();
            bd = nextInt();
            s1 = nextLong();
            as = nextLong();
            bs = nextLong();
            // end clean
        }

        private void solve(Result result) throws IOException {
            // todo calculate and fill result fields ===================================================================
            // begin clean
            long[][] cnt = new long[n + 2][3];
            long[] cnt2 = new long[n + 2];
            long[] prefixRes = new long[n + 2];
            prefixRes[0] = 1;
            for (int i = 1; i <= n; i++) {
                cnt[i][1] = 1;
                prefixRes[i] = 1;
            }
            long res = 0;
            for (int step = 0; step < m; step++) {
                if ((m / 100) > 0 && step % (m / 100) == 0) log(">>>> test num " + result.testNum + " step " + step);
                z1 = max(1, min(n, w1 + d1 - 1));

                cnt[w1][z1 - w1 + 1] = (cnt[w1][z1 - w1 + 1] + s1) % mm;
                if (z1 < w1) {
                    cnt2[w1] = mult(cnt[w1][0], cnt[w1 - 1][2]);
                } else if (z1 > w1) {
                    cnt2[w1 + 1] = mult(cnt[w1 + 1][0], cnt[w1][2]);
                }
                for (int i = w1; i <= n; i++) {
                    long t = mult(prefixRes[i - 1], cnt[i][1]);
                    if (i - 2 >= 0) {
                        t += mult(prefixRes[i - 2], cnt2[i]);
                        if (t >= mm) t -= mm;
                    }
                    prefixRes[i] = t;
                }
                res = (res + prefixRes[n]) % mm;
//                out.println(w1 + " " + z1 + " " + s1);

                w1 = (int) ((aw * (long) w1 + bw) % n + 1);
                d1 = (ad * d1 + bd) % 3;
                s1 = (as * s1 + bs) % 1000000000 + 1;
            }
            result.res = res;
            // end clean
        }

        @Override
        public Result call() throws Exception {
            int testNum;
            inLock.lock();
            try {
                testNum = ++lastTestNum;
                readData();
            } finally {
                inLock.unlock();
            }

            Result result = new Result(testNum);
            solve(result);
            log(">>> Solved test #" + testNum);
            return result;
        }
    }

    private static String inputFilename = "src/input.txt";
    private static String outputFilename = "src/output.txt";
    private BufferedReader in;
    private StringTokenizer line;
    private PrintWriter out;
    private boolean isDebug;
    private MyTimer timer = new MyTimer();

    private int lastTestNum = 0;
    private final Lock inLock = new ReentrantLock();
    private final Lock logLock = new ReentrantLock();

    public MultithreadedSolutionC(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public static void main(String[] args) throws IOException {
        new MultithreadedSolutionC(Arrays.asList(args).contains("DEBUG_MODE")).run();
//        new MultithreadedSolutionC(false).run();
    }

    private void run() throws IOException {
        if (isDebug) {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilename)));
//            in = new BufferedReader(new InputStreamReader(System.in));
        } else {
            in = new BufferedReader(new InputStreamReader(System.in));
        }
//        out = new PrintWriter(System.out);
        out = new PrintWriter(outputFilename);

        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        log(">>> Thread pool size: " + threadPoolSize);
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        int t = nextInt();

        ArrayList<Future<Result>> futureResults = new ArrayList<Future<Result>>(t);
        for (int i = 0; i < t; i++) {
            futureResults.add(executorService.submit(new Solver()));
        }

        ArrayList<Result> results = new ArrayList<Result>(t);
        for (Future<Result> f : futureResults) {
            try {
                results.add(f.get());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        executorService.shutdown();

        Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.testNum - o2.testNum;
            }
        });

        for (Result r : results) {
            printResult(r);
        }

        in.close();
        out.flush();
        out.close();
    }

    private void log(String s) throws IOException {
        if (isDebug) {
            logLock.lock();
            try {
                System.out.println(timer.getStr() + " " + s);
                System.out.flush();
            } finally {
                logLock.unlock();
            }
        }
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
            res[i] = nextInt();
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

    private String nextToken() throws IOException {
        while (line == null || !line.hasMoreTokens()) {
            line = new StringTokenizer(in.readLine());
        }
        return line.nextToken();
    }

    private static class MyTimer {
        private long lastTime = 0;

        private MyTimer() {
            reset();
        }

        public void reset() {
            lastTime = System.currentTimeMillis();
        }

        public long getMillisAndReset() {
            long current = System.currentTimeMillis();
            long result = current - lastTime;
            lastTime = current;
            return result;
        }

        public String getStrAndReset() {
            return String.format(Locale.ENGLISH, "%.3fs", getMillisAndReset() / 1000.0);
        }

        public long getMillis() {
            return System.currentTimeMillis() - lastTime;
        }

        public String getStr() {
            return String.format(Locale.ENGLISH, "%.3fs", getMillis() / 1000.0);
        }
    }
}
