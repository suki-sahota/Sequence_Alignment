package sequencealignment.efficient;

import sequencealignment.basic.Basic;
import sequencealignment.symbol.Symbol;
import sequencealignment.utils.Utilities;

import java.lang.Math;
import java.lang.StringBuilder;

public class Efficient {

    private static boolean outermost = true; // False after first recursive call
    private static int score = -1;
    private static final String FILENAME = "Efficient.java";

    /* A memory efficient version of sequence alignment
     * problem that uses O(m + n) space. The runtime is
     * still O(m * n). It is called recursively.
     * input: X is the first string to be aligned
     * input: Y is the second string to be aligned
     * output: ans is an array with two aligned strings
     */
    public static String[] alignment(String X, String Y) {
        Utilities.averageMemoryUsage();

        int m = X.length();
        int n = Y.length();

        Utilities.myAssert(m >= 0 && n >= 0, 29, FILENAME);

        String A = ""; // Alignment for string X
        String B = ""; // Alignment for string Y

        if (m <= 2 || n <= 2) {
            String[] res = Basic.alignment(X, Y); // Using API from basic version
            Utilities.myAssert(res.length == 2, 36, FILENAME);
            A = res[0];
            B = res[1];
        } else {
            int mid = n / 2;
            // Y[1:mid] => Y[0] to Y[mid - 1]
            int[] F = spaceEfficientAlignment(X, Y.substring(0, mid));
            // Y[mid+1:n] => Y[mid] to Y[n - 1]
            int[] G = backwardSpaceEfficientAlignment(X, Y.substring(mid));

            // Records best f(q, n/2) + g(q+1, (n/2)+1)
            int qBest = Integer.MIN_VALUE; 
            int q = -1; // Index for Divide and Conquer step that follows
            for (int i = 0; i <= m; ++i) {
                if (F[i] + G[i] > qBest) {
                    q = i;
                    qBest = F[i] + G[i];
                }
            }
            // Index for D&C must be found
            Utilities.myAssert(q > -1, 56, FILENAME);

            // Record score only if we are looking at first two subproblems
            if (outermost == true) {
                // We are in the outermost/first recursive call
                score = -qBest;
                outermost = false;
            }
            
            // Note: No need to worry about global P list in our implementation

            String[] left = alignment(
                // X[1:q] => X[0] to X[q - 1]
                // Y[1:mid] => Y[0] to Y[mid - 1]
                X.substring(0, q), Y.substring(0, mid) 
            );
            String[] right = alignment(
                // X[q+1:m] => X[q] to X[m - 1]
                // Y[mid+1:n] => Y[mid] to Y[n - 1]
                X.substring(q), Y.substring(mid) 
            );
            Utilities.myAssert(left.length == 2, 77, FILENAME);
            Utilities.myAssert(right.length == 2, 78, FILENAME);
            Utilities.myAssert(left[0].length() > 0 && left[1].length() > 0,
                80,
                FILENAME
            );
            Utilities.myAssert(
                right[0].length() > 0 && right[1].length() > 0,
                85,
                FILENAME
            );

            A = left[0] + right[0];
            B = left[1] + right[1];
        }
        String[] ans = new String[] { A, B };
        Utilities.myAssert(ans.length == 2, 93, FILENAME);

        Utilities.averageMemoryUsage();

        return ans;
    }

    /* Space efficient alignment algorithm that
     * works forward from (0, 0) to (m, n) and
     * returns an int array of optimal values
     * along a horizontal plane of the OPT grid.
     * input: X is the first string to be aligned
     * input: Y is the second string to be aligned
     * return: cur is the last column in our OPT grid
     */
    private static int[] spaceEfficientAlignment(String X, String Y) {
        Utilities.averageMemoryUsage();

        int m = X.length();
        int n = Y.length();

        // Two columns used instead of O(m * n) space
        int[] prev = new int[m + 1]; // Column 0 in efficient approach
        int[] cur = new int[m + 1];  // Column 1 in efficient approach

        // Initialize Base cases for all gaps in Y so far
        for (int i = 0; i <= m; ++i) {
             prev[i] = i * Utilities.GAP;
        }

        // Dynamic Programming algorithm
        for (int j = 1; j <= n; ++j) {
            cur[0] = j * Utilities.GAP; // Base case for all gaps in X so far
            for (int i = 1; i <= m; ++i) {
                // Retrieve symbols from enumeration class
                Symbol a = Symbol.valueOfLabel(X.charAt(i - 1));
                Symbol b = Symbol.valueOfLabel(Y.charAt(j - 1));

                int mismatch_cost = 
                    Utilities.MISMATCH[a.getIndex()][b.getIndex()];

                // Recurrence relation to solve sub problem
                int subproblem = 
                    Math.max(
                        prev[i - 1] + mismatch_cost, 
                        Math.max(
                            cur[i - 1] + Utilities.GAP,
                            prev[i] + Utilities.GAP
                        )
                    );
                
                // Memoize step
                cur[i] = subproblem;
            }
            prev = cur.clone(); // Move column 1 to column 0
        }

        Utilities.averageMemoryUsage();

        return cur;
    }

    /* Space efficient alignment algorithm that
     * works backwards from (m, n) to (0, 0) and
     * returns an int array of optimal values
     * along a horizontal plane of the OPT grid.
     * input: X is the first string to be aligned
     * input: Y is the second string to be aligned
     * return: cur is the last column in OPT grid when traversing backwards
     */
    private static int[] backwardSpaceEfficientAlignment(String X, String Y) {
        Utilities.averageMemoryUsage();

        int m = X.length();
        int n = Y.length();

        // Two columns used instead of O(m * n) space
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];

        // Initialize Base cases for all gaps in Y so far
        for (int i = m; i >= 0; --i) {
             prev[i] = (m - i) * Utilities.GAP;
        }

        // Dynamic Programming algorithm
        for (int j = n - 1; j >= 0; --j) {
            cur[m] = (n - j) * Utilities.GAP; // Base case for all gaps in X so far
            for (int i = m - 1; i >= 0; --i) {
                // Retrieve symbols from enumeration class
                Symbol a = Symbol.valueOfLabel(X.charAt(i));
                Symbol b = Symbol.valueOfLabel(Y.charAt(j));

                int mismatch_cost =
                    Utilities.MISMATCH[a.getIndex()][b.getIndex()];

                // Recurrence relation to solve sub problem
                int subproblem = 
                    Math.max(
                        prev[i + 1] + mismatch_cost, 
                        Math.max(
                            cur[i + 1] + Utilities.GAP,
                            prev[i] + Utilities.GAP
                        )
                    );
                
                // Memoize step
                cur[i] = subproblem;
            }
            prev = cur.clone(); // Move column 1 to column 0
        }

        Utilities.averageMemoryUsage();

        return cur;
    }

    public static int getScore() {
        return score;
    }
}
