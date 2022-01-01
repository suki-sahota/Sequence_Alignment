import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.Math;
import java.lang.StringBuilder;

import java.util.HashMap;
import java.util.Map;

class Basic_Alignment {
    
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
    private static final int MAX_LEN = 50;
    private static final int[][] MISMATCH = { {   0, -110,  -48,  -94},
                                              {-110,    0, -118,  -48},
                                              { -48, -118,    0, -110},
                                              { -94,  -48, -110,    0} };
    private static final String OUTPUT_FILENAME = "output.txt";
    private static final double THOUSAND = 1000.0;

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
     * two strings from given input file, then performs a
     * basic sequence alignment to produce two sequence-
     * aligned strings.
     */
    public static void main(String[] args) {
        startTime = System.currentTimeMillis(); // Record start time

        System.gc(); // Run garbage collector for more accurate memory picture
        
        averageMemoryUsage(); // This function is called multiple times

        myAssert(args.length == 1, 105); // Must only accept one commandline argument

        // Reads input
        readInput(args[0]);

        // Performs sequence alignmnet
        String[] alignedStrings = alignment();

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
            myAssert(base_one != null, 145);
            int j = 0; // counts next j lines and used for validation later
            cumulative_one.append(base_one);

            while ((str = br.readLine()) != null) {
                if (Character.isDigit(str.charAt(0))) {
                    // Continue building first cumulative string
                    ++j;
                    int idx = Integer.parseInt(str);
                    cumulative_one = generateInputString(cumulative_one, idx);
                    myAssert(validateInputString(
                            cumulative_one.length(), j, base_one.length()), 156);
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
        myAssert(0 < cumulative_one.length(), 169);

        // Build second string
        String base_two = "";
        StringBuilder cumulative_two = new StringBuilder("");

        try {
            base_two = str;
            myAssert(base_two != null, 177);
            myAssert(!Character.isDigit(base_two.charAt(0)), 178);
            int k = 0; // counts next k lines and used for validation later
            cumulative_two.append(base_two);

            while ((str = br.readLine()) != null) {
                if (Character.isDigit(str.charAt(0))) {
                    // Continue building second cumulative string
                    ++k;
                    int idx = Integer.parseInt(str);
                    cumulative_two = generateInputString(cumulative_two, idx);
                    myAssert(validateInputString(
                        cumulative_two.length(), k, base_two.length()), 189);
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
        myAssert(0 < cumulative_two.length(), 202);

        // Close Buffered Reader
        try {
            br.close();
        } catch (IOException err) {
            System.err.println("An error occurred closing BufferedReader.");
            err.printStackTrace();
            System.exit(1);
        }

        averageMemoryUsage(); // This function is called multiple times
    }

    /* This is the meat of the program. This function
     * is where we sequence align our two strings. 
     * input: none
     * return: alignedStrings which holds our aligned strings
     */
    private static String[] alignment() {
        averageMemoryUsage(); // This function is called multiple times

        int m = firstStr.length();
        int n = secondStr.length();

        // TEST PRINTING
        // int sum = m + n;
        // System.out.println("m+n: " + sum);

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
                Symbol a = Symbol.valueOfLabel(firstStr.charAt(i - 1));
                Symbol b = Symbol.valueOfLabel(secondStr.charAt(j - 1));

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
        String[] alignedStrings = constructAlignmentStrings(OPT);

        averageMemoryUsage(); // This function is called multiple times

        return alignedStrings;
    }

    /* This function takes the solved DP 2-D array and
     * actually constructs sequence aligned strings.
     * Multiple sequence aligned strings may be correct
     * if they all have the same final score in OPT[m][n].
     * input: OPT is the solved DP 2-D array
     * return: alignedStrings is an array of two strings
     */
    private static String[] constructAlignmentStrings(int[][] OPT) {
        averageMemoryUsage(); // This function is called multiple times

        StringBuilder firstResult = new StringBuilder("");
        StringBuilder secondResult = new StringBuilder("");
        int m = firstStr.length();
        int n = secondStr.length();
        int i = m;
        int j = n;

        while (i > 0 || j > 0) {
            // Retrieve symbols from enumeration class
            Symbol a = i > 0 ?
                Symbol.valueOfLabel(firstStr.charAt(i - 1)) : null;
            Symbol b = j > 0 ?
                Symbol.valueOfLabel(secondStr.charAt(j - 1)) : null;

            int mismatch_cost = (i > 0) && (j > 0) ?
                MISMATCH[a.index][b.index] : -1;

            if (i > 0 && j > 0 &&
                OPT[i][j] == OPT[i - 1][j - 1] + mismatch_cost)
            {
                // Both symbols used in this step
                firstResult.append(firstStr.charAt(i - 1));
                secondResult.append(secondStr.charAt(j - 1));
                --i;
                --j;
            } else if (i > 0 && OPT[i][j] == OPT[i - 1][j] + GAP) {
                // Symbol from first string used with gap from second string
                firstResult.append(firstStr.charAt(i - 1));
                secondResult.append("_");
                --i;
            } else {
                // Symbol from second string used with gap from first string
                firstResult.append("_");
                secondResult.append(secondStr.charAt(j - 1));
                --j;
            }
        }
        // Each string must be at least max(m, n) but not longer than m + n
        myAssert(Math.max(m, n) <= firstResult.length() &&
		 firstResult.length() <= m + n, 322);
        myAssert(Math.max(m, n) <= secondResult.length() &&
		 secondResult.length() <= m + n, 324);
        myAssert(firstResult.length() == secondResult.length(), 325);

        // Reverse strings since we built them backwards
        firstResult.reverse();
        secondResult.reverse();

        // Save sequence alignment score in global variable
        score = -OPT[m][n];

        // Create string array to hold two sequence aligned strings
        String[] alignedStrings = new String[] { firstResult.toString(),
                                                 secondResult.toString() };
        // Must only return two strings in array
        myAssert(alignedStrings.length == 2, 338);

        averageMemoryUsage(); // This function is called multiple times
        
        return alignedStrings;
    }

    /* Writes aligned strings, score, memory, and time to the 
     * "output.txt" file. Output will be graded by instructors.
     * input: alignedStrings is an array with two aligned strings
     * return: none
     */
    private static void writeOutput(String[] alignedStrings) {
        averageMemoryUsage(); // This function is called multiple times

        myAssert(alignedStrings.length == 2, 353);
        int m = firstStr.length();
        int n = secondStr.length();

        myAssert(Math.max(m, n) <= alignedStrings[0].length() && 
            alignedStrings[0].length() <= m + n, 358);
        myAssert(Math.max(m, n) <= alignedStrings[1].length() && 
            alignedStrings[1].length() <= m + n, 360);
        myAssert(alignedStrings[0].length() == alignedStrings[1].length(), 361);
        
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
        myWriter.write(alignedStrings[0].substring(0, MAX_LEN));
        myWriter.write(" ");
        myWriter.write(alignedStrings[0]
            .substring(alignedStrings[0].length() - MAX_LEN)
        );
        myWriter.write("\n");

        // Write first and last 50 characters of second string
        myWriter.write(alignedStrings[1].substring(0, MAX_LEN));
        myWriter.write(" ");
        myWriter.write(alignedStrings[1]
            .substring(alignedStrings[1].length() - MAX_LEN)
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
        myAssert(0 <= index && index < base.length(), 479); // Check for valid insert index
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
