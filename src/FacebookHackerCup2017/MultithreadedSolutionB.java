package FacebookHackerCup2017;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MultithreadedSolutionB {
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
        private double res;
        // end clean
    }

    private void printResult(Result result) throws IOException {
        out.print("Case #" + result.testNum + ": ");

        // todo print result of one test ===============================================================================
        // begin clean
        out.println(String.format(Locale.ENGLISH, "%.7f", result.res));
        // end clean
    }

    // begin clean

    // end clean


    private class Solver implements Callable<Result> {
        // todo add fields for input parameters ========================================================================
        // begin clean
        private int n;
        private long x1, ax, bx, cx;
        private long h1, ah, bh, ch;

        private double intersect(long x0, long h0, long x1, long h1) {
            if (x0 > x1) return intersect(x1, h1, x0, h0);
            long a = h0 + h1 - (x1 - x0);
            if (a <= 0) {
                return 0;
            }
            return (a / 2.0) * (a / 2.0);
        }

        private double remove(TreeMap<Long, Long> a, long x, long h) {
            a.remove(x);
            Map.Entry<Long, Long> left = a.floorEntry(x);
            Map.Entry<Long, Long> right = a.ceilingEntry(x);
            if (left == null) {
                if (right == null) {
                    return h * h;
                } else {
                    return h * h - intersect(x, h, right.getKey(), right.getValue());
                }
            } else {
                if (right == null) {
                    return h * h - intersect(left.getKey(), left.getValue(), x, h);
                } else {
                    return h * h
                            - intersect(x, h, right.getKey(), right.getValue())
                            - intersect(left.getKey(), left.getValue(), x, h)
                            + intersect(left.getKey(), left.getValue(), right.getKey(), right.getValue());
                }
            }
        }

        private double add(TreeMap<Long, Long> a, long x, long h) {
            double res = 0;
            while (true) {
                Map.Entry<Long, Long> e = a.floorEntry(x);
                if (e == null) break;
                long ex = e.getKey();
                long eh = e.getValue();
                if (h - Math.abs(x - ex) >= eh) {
                    res -= remove(a, ex, eh);
                } else {
                    if (eh - Math.abs(x - ex) >= h) {
                        return res;
                    } else {
                        break;
                    }
                }
            }
            while (true) {
                Map.Entry<Long, Long> e = a.ceilingEntry(x);
                if (e == null) break;
                long ex = e.getKey();
                long eh = e.getValue();
                if (h - Math.abs(x - ex) >= eh) {
                    res -= remove(a, ex, eh);
                } else {
                    if (eh - Math.abs(x - ex) >= h) {
                        return res;
                    } else {
                        break;
                    }
                }
            }
            a.put(x, h);
            res += remove(a, x, h);
            a.put(x, h);
            return res;
        }

        // end clean

        private void readData() throws IOException {
            // todo read input parameters ==============================================================================
            // begin clean
            n = nextInt();
            x1 = nextLong();
            ax = nextLong();
            bx = nextLong();
            cx = nextLong();
            h1 = nextLong();
            ah = nextLong();
            bh = nextLong();
            ch = nextLong();
            // end clean
        }

        private void solve(Result result) throws IOException {
            // todo calculate and fill result fields ===================================================================
            // begin clean
            TreeMap<Long, Long> a = new TreeMap<>();
            double[] res = new double[n];

            for (int z = 0; z < n; z++) {
                res[z] = add(a, x1, h1);
                x1 = (ax * x1 + bx) % cx + 1;
                h1 = (ah * h1 + bh) % ch + 1;
            }

            for (int i = 0; i < n; i++) {
                res[i] *= (n - i);
            }

            Arrays.sort(res);
            double rr = 0;
            for (double i : res) {
                rr += i;
            }
            result.res = rr;
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

    public MultithreadedSolutionB(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public static void main(String[] args) throws IOException {
        new MultithreadedSolutionB(Arrays.asList(args).contains("DEBUG_MODE")).run();
//        new MultithreadedSolutionB(false).run();
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
