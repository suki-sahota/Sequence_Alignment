import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.Math;
import java.lang.StringBuilder;

import java.util.HashMap;
import java.util.Map;

class Efficient_4264649669_5807825570 {
    
    // ========================================================================
    // ------------------------- Start of Enumeration -------------------------
    // ========================================================================
    /* Enumeration for the symbols used in this project.
     * Contains custom value fields, constructor, and
     * getter methods for constant time access. These
     * four letters will be used during the sequence
     * alignment.
     */
    public enum Symbol {
        // Symbol Character and index
        A('A', 0),
        C('C', 1),
        G('G', 2),
        T('T', 3);

        // Enumeration fields
        public final Character label;
        public final int index;

        // Map for constant access to Symbols from fields
        private static final Map<Character, Symbol> BY_LABEL = new HashMap<>();
        private static final Map<Integer, Symbol> BY_INDEX = new HashMap<>();

        // Populates when class loads
        static {
            for (Symbol s : values()) {
                BY_LABEL.put(s.label, s);
                BY_INDEX.put(s.index, s);
            }
        }

        // Constructor to add more symbols (if we want to extend problem)
        private Symbol(Character label, int index) {
            this.label = label;
            this.index = index;
        }

        // Customized valueOf() method for label
        public static Symbol valueOfLabel(Character label) {
            return BY_LABEL.get(label);
        }

        // Customized valueOf() method for index
        public static Symbol valueOfIndex(int index) {
            return BY_INDEX.get(index);
        }
    }
    // ========================================================================
    // -------------------------- End of Enumeration --------------------------
    // ========================================================================

    // ========================================================================
    // ---------------------- Start of Global Variables -----------------------
    // ========================================================================
    private static final int GAP = -30;
    private static final int KILO = 1024;
    //private static final int MAX_INPUT_LEN = 1024;
    private static final int MAX_OUTPUT_LEN = 50;
    private static final int[][] MISMATCH = { {   0, -110,  -48,  -94},
                                              {-110,    0, -118,  -48},
                                              { -48, -118,    0, -110},
                                              { -94,  -48, -110,    0} };
    private static final String OUTPUT_FILENAME = "output.txt";
    private static final double THOUSAND = 1000.0;

    private static boolean outermost = true; // False after first recursive call
    private static String firstStr = "";  // first cumulative string to align
    private static String secondStr = ""; // second cumulative string to align
    private static int score = 0;         // total alignment cost (positive)
    private static long startTime = 0;
    private static double memory = 0.0;    // average memory program is using
    private static int numCountMemory = 0; // number of times taking average
    // ========================================================================
    // ----------------------- End of Global Variables ------------------------
    // ========================================================================

    // ========================================================================
    // ------------------------ Start of Main Process -------------------------
    // ========================================================================
    /* Main function to drive program. This program creates
     * two strings from given input file, then performs an
     * efficient sequence alignment to produce two sequence-
     * aligned strings.
     */
    public static void main(String[] args) {
        startTime = System.currentTimeMillis(); // Record start time

        System.gc(); // Run garbage collector for more accurate memory picture

        averageMemoryUsage(); // This function is called multiple times

        myAssert(args.length == 1, 107); // Must only accept one commandline argument

        // Reads input
        readInput(args[0]);

        // Performs efficient sequence alignment
        String[] alignedStrings = efficientAlignment(firstStr, secondStr);
        
        // Writes output
        writeOutput(alignedStrings);

        System.exit(0); // Success exit code
    }
    // ========================================================================
    // ------------------------- End of Main Process --------------------------
    // ========================================================================

    /* Reads input file and creates the two strings
     * that we will be working with during this program.
     * We save those two strings into the global variables
     * firstStr and secondStr to be used by rest of program.
     * input: input_file_path is the name of input file
     * return: none
     */
    private static void readInput(String input_file_path) {
        averageMemoryUsage(); // This function is called multiple times

        File input_file = null;
        BufferedReader br = null;
        String str = "";

        // Build first string
        String base_one = "";
        StringBuilder cumulative_one = new StringBuilder("");

        try {
            input_file = new File(input_file_path);
            br = new BufferedReader(new FileReader(input_file));
            
            base_one = br.readLine();
            myAssert(base_one != null, 147);
            int j = 0; // counts next j lines and used for validation later
            cumulative_one.append(base_one);

            while ((str = br.readLine()) != null) {
                if (Character.isDigit(str.charAt(0))) {
                    // Continue building first cumulative string
                    ++j;
                    int idx = Integer.parseInt(str);
                    cumulative_one = generateInputString(cumulative_one, idx);
                    myAssert(validateInputString(
                            cumulative_one.length(), j, base_one.length()), 158);
                    myAssert(0 < cumulative_one.length(), 159);
                } else {
                    // Start building second base string
                    break;
                }
            }
        } catch (IOException err) {
            System.err.println("An error occurred building the first string.");
            err.printStackTrace();
            System.exit(1);
        }
        // Finished building first string
        firstStr = cumulative_one.toString();
        myAssert(0 < cumulative_one.length(), 172);

        // Build second string
        String base_two = "";
        StringBuilder cumulative_two = new StringBuilder("");

        try {
            base_two = str;
            myAssert(base_two != null, 180);
            myAssert(!Character.isDigit(base_two.charAt(0)), 181);
            int k = 0; // counts next k lines and used for validation later
            cumulative_two.append(base_two);

            while ((str = br.readLine()) != null) {
                if (Character.isDigit(str.charAt(0))) {
                    // Continue building second cumulative string
                    ++k;
                    int idx = Integer.parseInt(str);
                    cumulative_two = generateInputString(cumulative_two, idx);
                    myAssert(validateInputString(
						 cumulative_two.length(), k, base_two.length()), 192);
                    myAssert(0 < cumulative_two.length(), 193);
                } else {
                    System.err.println("PANIC: Read in a third base string");
                    System.exit(1); // exit the program with failure code 1
                }
            }
        } catch (IOException err) {
            System.err.println("An error occurred building the second string.");
            err.printStackTrace();
            System.exit(1);
        }
        // Finished building second string
        secondStr = cumulative_two.toString();
        myAssert(0 < cumulative_two.length(), 206);

        // Close Buffered Reader
        try {
            br.close();
        } catch (IOException err) {
            System.err.println("An error occurred closing BufferedReader.");
            err.printStackTrace();
            System.exit(1);
        }

        // TEST PRINTING
	    // int sum = cumulative_one.length() + cumulative_two.length();
	    // System.out.println("m+n: " + sum);

        averageMemoryUsage(); // This function is called multiple times
    }

    /* A memory efficient version of sequence alignment
     * problem that uses O(m + n) space. The runtime is
     * still O(m * n). It is called recursively.
     * input: X is the first string to be aligned
     * input: Y is the second string to be aligned
     * output: ans is an array with two aligned strings
     */
    private static String[] efficientAlignment(String X, String Y) {
        averageMemoryUsage(); // This function is called multiple times

        int m = X.length();
        int n = Y.length();

        myAssert(m >= 0 && n >= 0, 237);

        String A = ""; // Alignment for string X
        String B = ""; // Alignment for string Y

        if (m <= 2 || n <= 2) {
            String[] res = alignment(X, Y); // Using API from basic version
            myAssert(res.length == 2, 244);
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
            myAssert(q > -1, 263); // Index for D&C must be found

            // Record score only if we are looking at first two subproblems
            if (outermost == true) {
                // We are in the outermost/first recursive call
                score = -qBest;
                outermost = false;
            }
            
            // Note: No need to worry about global P list in our implementation

            String[] left = efficientAlignment(
                // X[1:q] => X[0] to X[q - 1]
                // Y[1:mid] => Y[0] to Y[mid - 1]
                X.substring(0, q), Y.substring(0, mid) 
            );
            String[] right = efficientAlignment(
                // X[q+1:m] => X[q] to X[m - 1]
                // Y[mid+1:n] => Y[mid] to Y[n - 1]
                X.substring(q), Y.substring(mid) 
            );
            myAssert(left.length == 2, 284);
            myAssert(right.length == 2, 285);
            myAssert(left[0].length() > 0 && left[1].length() > 0, 286);
            myAssert(right[0].length() > 0 && right[1].length() > 0, 287);

            A = left[0] + right[0];
            B = left[1] + right[1];
        }
        String[] ans = new String[] { A, B };
        myAssert(ans.length == 2, 293);

        averageMemoryUsage(); // This function is called multiple times

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
        averageMemoryUsage(); // This function is called multiple times

        int m = X.length();
        int n = Y.length();

        // Two columns used instead of O(m * n) space
        int[] prev = new int[m + 1]; // Column 0 in efficient approach
        int[] cur = new int[m + 1];  // Column 1 in efficient approach

        // Initialize Base cases for all gaps in Y so far
        for (int i = 0; i <= m; ++i) {
             prev[i] = i * GAP;
        }

        // Dynamic Programming algorithm
        for (int j = 1; j <= n; ++j) {
            cur[0] = j * GAP; // Base case for all gaps in X so far
            for (int i = 1; i <= m; ++i) {
                // Retrieve symbols from enumeration class
                Symbol a = Symbol.valueOfLabel(X.charAt(i - 1));
                Symbol b = Symbol.valueOfLabel(Y.charAt(j - 1));

                int mismatch_cost = MISMATCH[a.index][b.index];

                // Recurrence relation to solve sub problem
                int subproblem = 
                    Math.max(
                        prev[i - 1] + mismatch_cost, 
                        Math.max(cur[i - 1] + GAP, prev[i] + GAP)
                    );
                
                // Memoize step
                cur[i] = subproblem;
            }
            prev = cur.clone(); // Move column 1 to column 0
        }

        averageMemoryUsage(); // This function is called multiple times

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
        averageMemoryUsage(); // This function is called multiple times

        int m = X.length();
        int n = Y.length();

        // Two columns used instead of O(m * n) space
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];

        // Initialize Base cases for all gaps in Y so far
        for (int i = m; i >= 0; --i) {
             prev[i] = (m - i) * GAP;
        }

        // Dynamic Programming algorithm
        for (int j = n - 1; j >= 0; --j) {
            cur[m] = (n - j) * GAP; // Base case for all gaps in X so far
            for (int i = m - 1; i >= 0; --i) {
                // Retrieve symbols from enumeration class
                Symbol a = Symbol.valueOfLabel(X.charAt(i));
                Symbol b = Symbol.valueOfLabel(Y.charAt(j));

                int mismatch_cost = MISMATCH[a.index][b.index];

                // Recurrence relation to solve sub problem
                int subproblem = 
                    Math.max(
                        prev[i + 1] + mismatch_cost, 
                        Math.max(cur[i + 1] + GAP, prev[i] + GAP)
                    );
                
                // Memoize step
                cur[i] = subproblem;
            }
            prev = cur.clone(); // Move column 1 to column 0
        }

        averageMemoryUsage(); // This function is called multiple times

        return cur;
    }

    /* This is the algorithm used in the basic version with
     * some minor modifications so that it can be used 
     * during the space efficient version. This function is
     * essentially the base case during the divide and conquer
     * step of the memory efficient verison.
     * input: X is the first string to be aligned
     * input: Y is the second string to be aligned
     * return: alignedStrings which holds our aligned strings
     */
    private static String[] alignment(String X, String Y) {
        averageMemoryUsage(); // This function is called multiple times

        int m = X.length();
        int n = Y.length();

        // Allocate memory for our 2-D array used for memoization
          // OPT[1][1] optimal solution when using 1 character from each string
          // OPT[m][n] optimal solution for our entire sequence alignment
        int[][] OPT = new int[m + 1][n + 1];

        // Initialize array with base cases
        for (int i = 0; i <= m; ++i) {
            for (int j = 0; j <= n; ++j) {
                if (i == 0) { OPT[i][j] = j * GAP; }
                if (j == 0) { OPT[i][j] = i * GAP; }
            }
        }

        // Execute Dynamic Programming algorithm
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                // Retrieve symbols from enumeration class
                Symbol a = Symbol.valueOfLabel(X.charAt(i - 1));
                Symbol b = Symbol.valueOfLabel(Y.charAt(j - 1));

                int mismatch_cost = MISMATCH[a.index][b.index];

                // Recurrence relation to solve sub problem
                int subproblem = 
                    Math.max(
                        OPT[i - 1][j - 1] + mismatch_cost, 
                        Math.max(OPT[i - 1][j] + GAP, OPT[i][j - 1] + GAP)
                    );
                
                // Memoize step
                OPT[i][j] = subproblem;
            }
        }
        
        // Create sequence aligned strings from solved DP 2-D array
        String[] alignedStrings = constructAlignmentStrings(OPT, X, Y);

        averageMemoryUsage(); // This function is called multiple times

        return alignedStrings;
    }

    /* This function takes the solved DP 2-D array and
     * actually constructs sequence aligned strings.
     * Multiple sequence aligned strings may be correct
     * if they all have the same final score in OPT[m][n].
     * input: OPT is the solved DP 2-D array
     * input: X is the first string that needed to be aligned
     * input: Y is the second string that needed to be aligned
     * return: alignedStrings is an array of two aligned strings
     */
    private static String[] constructAlignmentStrings(int[][] OPT,
                                                      String X, String Y)
    {
        averageMemoryUsage(); // This function is called multiple times

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
                MISMATCH[a.index][b.index] : -1;

            if (i > 0 && j > 0 &&
                OPT[i][j] == OPT[i - 1][j - 1] + mismatch_cost)
            {
                // Both symbols used in this step
                firstResult.append(X.charAt(i - 1));
                secondResult.append(Y.charAt(j - 1));
                --i;
                --j;
            } else if (i > 0 && OPT[i][j] == OPT[i - 1][j] + GAP) {
                // Symbol from first string used with gap from second string
                firstResult.append(X.charAt(i - 1));
                secondResult.append("_");
                --i;
            } else {
                // Symbol from second string used with gap from first string
                firstResult.append("_");
                secondResult.append(Y.charAt(j - 1));
                --j;
            }
        }
        // Each string must be at least max(m, n) but not longer than m + n
        myAssert(Math.max(m, n) <= firstResult.length() &&
		 firstResult.length() <= m + n, 512);
        myAssert(Math.max(m, n) <= secondResult.length() &&
		 secondResult.length() <= m + n, 514);
        myAssert(firstResult.length() == secondResult.length(), 515);

        // Reverse strings since we built them backwards
        firstResult.reverse();
        secondResult.reverse();

        // We only save sequence alignment score here in basic version
        // score = -OPT[m][n];

        // Create string array to hold two sequence aligned strings
        String[] alignedStrings = new String[] { firstResult.toString(),
                                                 secondResult.toString() };
        // Must only return two strings in array
        myAssert(alignedStrings.length == 2, 528);
        
        averageMemoryUsage(); // This function is called multiple times

        return alignedStrings;
    }

    /* Writes strings, score, memory, and time to the 
     * "output.txt" file. Output will be graded by instructors.
     * input: alignedStrings is an array with two aligned strings
     * return: none
     */
    private static void writeOutput(String[] alignedStrings) {
        averageMemoryUsage(); // This function is called multiple times

        myAssert(alignedStrings.length == 2, 543);
        int m = firstStr.length();
        int n = secondStr.length();

        myAssert(Math.max(m, n) <= alignedStrings[0].length() && 
		 alignedStrings[0].length() <= m + n, 548);
        myAssert(Math.max(m, n) <= alignedStrings[1].length() && 
		 alignedStrings[1].length() <= m + n, 550);
        myAssert(alignedStrings[0].length() == alignedStrings[1].length(), 551);
        
        // Create new file
        File myObj = new File(OUTPUT_FILENAME);

        // Create FileWriter and write to file
        FileWriter myWriter = null;
        try {
            averageMemoryUsage(); // This function is called multiple times

            myWriter = new FileWriter(myObj);

            // Write aligned strings to output file
            writeStrings(alignedStrings, myWriter);

            // Write score for this alignment to output file
            writeScore(myWriter);

            // Write time in seconds to output file
            writeTime(myWriter);

            // Write memory in KiloBytes to output file
            writeMemory(myWriter);
        } catch (IOException err) {
            System.err.println("An error occurred writing to file.");
            err.printStackTrace();
            System.exit(1);
        }

        // Flush and close FileWriter
        try {
            myWriter.flush();
            myWriter.close();
        } catch (IOException err) {
            System.err.println("An error occurred closing the file.");
            err.printStackTrace();
        }
    }

    /* Writes first and last 50 characters to output file.
     * If a string is less than 50 characters, it will write
     * the entire string to output file.
     * input: alignedStrings is an array of two aligned strings
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeStrings(String[] alignedStrings,
                                     FileWriter myWriter) throws IOException
    {
        // Write first and last 50 characters of first string
        myWriter.write(alignedStrings[0].substring(0, MAX_OUTPUT_LEN));
        myWriter.write(" ");
        myWriter.write(alignedStrings[0]
            .substring(alignedStrings[0].length() - MAX_OUTPUT_LEN)
        );
        myWriter.write("\n");

        // Write first and last 50 characters of second string
        myWriter.write(alignedStrings[1].substring(0, MAX_OUTPUT_LEN));
        myWriter.write(" ");
        myWriter.write(alignedStrings[1]
            .substring(alignedStrings[1].length() - MAX_OUTPUT_LEN)
        );
        myWriter.write("\n");
    }

    /* Writes score to the output file.
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeScore(FileWriter myWriter) throws IOException {
        myWriter.write(String.valueOf(score));
        myWriter.write("\n");

        // TEST PRINTING
        // System.out.println("Cost: " + String.valueOf(score));
    }

    /* Writes time used to the output file in seconds.
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeTime(FileWriter myWriter) throws IOException {
        long endTime = System.currentTimeMillis();

        double elapsedTime = endTime - startTime; // Calculate in milliseconds
        elapsedTime /= THOUSAND;                  // Convert to seconds

        myWriter.write(String.valueOf(elapsedTime));
        myWriter.write("\n");

	    // TEST PRINTING
	    // System.out.println("Time: " + String.valueOf(elapsedTime));
    }

    /* Writes memory used to the output file in KiloBytes.
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeMemory(FileWriter myWriter) throws IOException {
        averageMemoryUsage(); // This function is called multiple times

        memory /= KILO; // convert to KiloBytes

        myWriter.write(String.valueOf(memory));
        myWriter.write("\n");

	    // TEST PRINTING
	    // System.out.println("Memory: " + String.valueOf(memory) + "\n");
    }

    /* Handles one iteration during the process
     * where we generate our cumulative string.
     * input: base StringBuilder where string is inserted during iteration
     * input: index where to insert base string
     * return: cumulative StringBuilder after one insertion
     */
    private static StringBuilder generateInputString(StringBuilder base, int index) {
        myAssert(0 <= index && index < base.length(), 669); // Check for valid insert index
        base.insert(index + 1, base.toString());
        return base;
    }

    /* Validates the length of each of our cumulative strings.
     * input: len is length of string being validated
     * input: i represents either j or k from the input
     * input: n is length of the original base string
     * return: truth value for whether string is valid
     */
    private static boolean validateInputString(int len, int i, int n) {
        return len == Math.pow(2, i) * n ? true : false;
    }

    /* Custom assert function to maintain proper
     * order of code. Ensures that all preconditions,
     * postconditions, and representation invariants
     * hold true. Expressions in this function
     * must evaluate to true, otherwise program exits.
     * input: value of the expression being asserted
     * return: none
     */
    private static void myAssert(boolean value, int lineNumber) {
        if (value == true) {
            // Value asserts properly
            return;
        } else {
            // Value does not assert correctly
            System.err.println("PANIC: value did not assert properly on line " + lineNumber);
            System.exit(1); // exit the program with failure code 1
        }
    }

    /* Keeps a running average of memory usage
     * in this program. This function can be called
     * upon at any point in the program. The running 
     * average is kept as a global variable.
     * input: none
     * return: none
     */
    private static void averageMemoryUsage() {
        // Calculate current memory usage
        double curr_memory = Runtime.getRuntime().totalMemory() -
            Runtime.getRuntime().freeMemory();
        // Calculate running average
        memory = ((memory * numCountMemory) + curr_memory) / (numCountMemory + 1);
        ++numCountMemory; // Increment count
    }
}
