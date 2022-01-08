package sequencealignment;

import sequencealignment.basic.Basic;
import sequencealignment.input.Input;
import sequencealignment.output.Output;
import sequencealignment.symbol.Symbol;
import sequencealignment.utils.Utilities;

class Basic_Alignment {

    private static final String FILENAME = "Basic_Alignment.java";

    /* Main function to drive program. This program creates
     * two strings from given input file, then performs a
     * basic sequence alignment to produce two sequence-
     * aligned strings.
     */
    public static void main(String[] args) {
        Utilities.setStartTime(System.currentTimeMillis()); // Record start time

        System.gc(); // Run garbage collector for more accurate memory picture
        
        Utilities.averageMemoryUsage();

        // Must only accept one commandline argument
        Utilities.myAssert(args.length == 1, 26, FILENAME);

        Input.readInput(args[0]);

        printProblemSize();

        String[] alignedStrings = Basic.alignment(
            Input.getFirstString(),
            Input.getSecondString()
        );

        Output.writeOutput(alignedStrings, 0);

        System.exit(0); // Success exit code
    }

    private static void printProblemSize() {
        int m = Input.getFirstString().length();
        int n = Input.getSecondString().length();
        int sum = m + n;
        System.out.println("m+n: " + sum);
    }
}
