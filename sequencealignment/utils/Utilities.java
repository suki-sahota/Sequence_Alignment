package sequencealignment.utils;

public class Utilities {

    public static final int GAP = -30;
    public static final int KILO = 1024;
    public static final int MAX_LEN = 50;
    public static final int[][] MISMATCH = { {   0, -110,  -48,  -94},
                                              {-110,    0, -118,  -48},
                                              { -48, -118,    0, -110},
                                              { -94,  -48, -110,    0} };
    public static final String OUTPUT_FILENAME = "output.txt";
    public static final double THOUSAND = 1000.0;

    public static double memory = 0.0;     // average memory program is using

    private static int numCountMemory = 0; // number of times taking average
    private static long startTime = -1;
    private static final String FILENAME = "Utilities.java";

    /* Custom assert function to maintain proper
     * order of code. Ensures that all preconditions,
     * postconditions, and representation invariants
     * hold true. Expressions in this function
     * must evaluate to true, otherwise program exits.
     * input: expression being asserted
     * input: linenumber is the line number from where this function was called
     * input: filename is name of the file from where this function was called
     * return: none
     */
    public static void myAssert(boolean expr, int lineNumber, String filename) {
        if (expr != true) {
            // Expression does not assert correctly
            System.err.println(
                "PANIC: expression did not assert properly on line "
                + lineNumber
                + " in file "
                + filename
            );
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
    public static void averageMemoryUsage() {
        // Calculate current memory usage
        double curr_memory = Runtime.getRuntime().totalMemory()
            - Runtime.getRuntime().freeMemory();
        // Calculate running average
        memory = ((memory * numCountMemory) + curr_memory)
            / (numCountMemory + 1);
        ++numCountMemory; // Increment count
    }

    public static void setStartTime(long time) {
        startTime = time;
    }

    public static long getStartTime() {
        return startTime;
    }
}
