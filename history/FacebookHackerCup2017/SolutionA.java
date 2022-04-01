package FacebookHackerCup2017;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

import static java.lang.Math.min;


public class SolutionA {
    private static String inputFilename = "src/input.txt";
    private static String outputFilename = "src/output.txt";
    private BufferedReader in;
    private StringTokenizer line;
    private PrintWriter out;
    private boolean isDebug;

    public SolutionA(boolean isDebug) {
        this.isDebug = isDebug;
    }

    private int INF = 1000000000;

    private int f1(int n, int m, int k) {
        if (n - 2 >= 2 * k + 1) {
            if (m > k) {
                return m / k + (m % k == 0 ? 0 : 1);
            }
        }
        return INF;
    }

    private int f2(int n, int m, int k) {
        if (k > 1) {
            if (m >= 3 * k + 1) {
                if (n >= 2 * k + 1) {
                    return 4;
                }
            }
        } else {
            if (m >= 5) {
                if (n >= 3) {
                    return 5;
                }
            }
        }
        return INF;
    }

    public void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        int k = nextInt();
        int res = min(
                min(f1(n, m, k), f1(m, n, k)),
                min(f2(n, m, k), f2(m, n, k))
        );
        if (res == INF) {
            res = -1;
        }
        out.println(res);
    }

    private static final int mm = 1000000007;

    private long mult(long a, long b) {
        return a * b % mm;
    }

    public static void main(String[] args) throws IOException {
        new SolutionA(Arrays.asList(args).contains("DEBUG_MODE")).run(args);
    }

    public void run(String[] args) throws IOException {
        if (isDebug) {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilename)));
//            in = new BufferedReader(new InputStreamReader(System.in));
        } else {
            in = new BufferedReader(new InputStreamReader(System.in));
        }
//        out = new PrintWriter(System.out);
        out = new PrintWriter(outputFilename);

        int t = nextInt();
//        int t = 1;
        for (int i = 0; i < t; i++) {
            out.print("Case #" + (i + 1) + ": ");
            solve();
        }

        in.close();
        out.flush();
        out.close();
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
}