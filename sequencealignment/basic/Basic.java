package sequencealignment.basic;

import sequencealignment.symbol.Symbol;
import sequencealignment.utils.Utilities;

public class Basic {

    private static int score = -1;
    private static final String FILENAME = "Basic.java";

    /* This is the meat of the program. This function
     * is where we sequence align our two strings. 
     * input: X is first string to be aligned
     * input: Y is second string to be aligned
     * return: alignedStrings which holds our aligned strings
     */
    public static String[] alignment(String X, String Y) {
        Utilities.averageMemoryUsage();

        int m = X.length();
        int n = Y.length();

        // Allocate memory for our 2-D array used for memoization
          // OPT[1][1] optimal solution when using 1 character from each string
          // OPT[m][n] optimal solution for our entire sequence alignment
        int[][] OPT = new int[m + 1][n + 1];

        // Initialize array with base cases
        for (int i = 0; i <= m; ++i) {
            for (int j = 0; j <= n; ++j) {
                if (i == 0) { OPT[i][j] = j * Utilities.GAP; }
                if (j == 0) { OPT[i][j] = i * Utilities.GAP; }
            }
        }

        // Execute Dynamic Programming algorithm
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                // Retrieve symbols from enumeration class
                Symbol a = Symbol.valueOfLabel(X.charAt(i - 1));
                Symbol b = Symbol.valueOfLabel(Y.charAt(j - 1));

                int mismatch_cost = 
                    Utilities.MISMATCH[a.getIndex()][b.getIndex()];

                // Recurrence relation to solve sub problem
                int subproblem = 
                    Math.max(
                        OPT[i - 1][j - 1] + mismatch_cost, 
                        Math.max(
                            OPT[i - 1][j] + Utilities.GAP,
                            OPT[i][j - 1] + Utilities.GAP
                        )
                    );
                
                // Memoize step
                OPT[i][j] = subproblem;
            }
        }

        // Create sequence aligned strings from solved DP 2-D array
        String[] alignedStrings = constructAlignmentStrings(OPT, X, Y);

        Utilities.averageMemoryUsage();

        return alignedStrings;
    }

    /* This function takes the solved DP 2-D array and
     * actually constructs sequence aligned strings.
     * Multiple sequence aligned strings may be correct
     * if they all have the same final score in OPT[m][n].
     * input: OPT is the solved DP 2-D array
     * input: X is first string to be aligned
     * input: Y is second string to be aligned
     * return: alignedStrings is an array of two strings
     */
    private static String[] constructAlignmentStrings(int[][] OPT,
                                                      String X, String Y)
    {
        Utilities.averageMemoryUsage();

        StringBuilder firstResult = new StringBuilder("");
        StringBuilder secondResult = new StringBuilder("");
        int m = X.length();
        int n = Y.length();
        int i = m;
        int j = n;

        while (i > 0 || j > 0) {
            // Retrieve symbols from enumeration class
            Symbol a = i > 0 ?
                Symbol.valueOfLabel(X.charAt(i - 1)) : null;
            Symbol b = j > 0 ?
                Symbol.valueOfLabel(Y.charAt(j - 1)) : null;

            int mismatch_cost = (i > 0) && (j > 0) ?
                Utilities.MISMATCH[a.getIndex()][b.getIndex()] : -1;

            if (i > 0 && j > 0 &&
                OPT[i][j] == OPT[i - 1][j - 1] + mismatch_cost)
            {
                // Both symbols used in this step
                firstResult.append(a.getLabel());
                secondResult.append(b.getLabel());
                --i;
                --j;
            } else if (i > 0 && OPT[i][j] == OPT[i - 1][j] + Utilities.GAP) {
                // Symbol from first string used with gap from second string
                firstResult.append(a.getLabel());
                secondResult.append("_");
                --i;
            } else {
                // Symbol from second string used with gap from first string
                firstResult.append("_");
                secondResult.append(b.getLabel());
                --j;
            }
        }
        // Each string must be at least max(m, n) but not longer than m + n
        Utilities.myAssert(
            Math.max(m, n) <= firstResult.length()
                && firstResult.length() <= m + n,
            128,
            FILENAME
        );
        Utilities.myAssert(
            firstResult.length() == secondResult.length(),
            133, 
            FILENAME
        );

        // Reverse strings since we built them backwards
        firstResult.reverse();
        secondResult.reverse();

        // Save sequence alignment score in global variable
        score = -OPT[m][n];

        // Create string array to hold two sequence aligned strings
        String[] alignedStrings = new String[] { firstResult.toString(),
                                                 secondResult.toString() };
        // Must only return two strings in array
        Utilities.myAssert(alignedStrings.length == 2, 148, FILENAME);

        Utilities.averageMemoryUsage();
        
        return alignedStrings;
    }

    public static int getScore() {
        return score;
    }
}
