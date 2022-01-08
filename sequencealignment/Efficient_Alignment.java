package sequencealignment;

import sequencealignment.basic.Basic;
import sequencealignment.efficient.Efficient;
import sequencealignment.input.Input;
import sequencealignment.output.Output;
import sequencealignment.symbol.Symbol;
import sequencealignment.utils.Utilities;

public class Efficient_Alignment {

    private static final String FILENAME = "Efficient_Alignment.java";

    public static void main(String[] args) {
        Utilities.setStartTime(System.currentTimeMillis()); // Record start time

        System.gc(); // Run garbage collector for more accurate memory picture
        
        Utilities.averageMemoryUsage();

        // Must only accept one commandline argument
        Utilities.myAssert(args.length == 1, 22, FILENAME);

        Input.readInput(args[0]);

        printProblemSize();

        String[] alignedStrings = Efficient.alignment(
            Input.getFirstString(),
            Input.getSecondString()
        );

        Output.writeOutput(alignedStrings, 1);

        System.exit(0); // Success exit code
    }

    private static void printProblemSize() {
        int m = Input.getFirstString().length();
        int n = Input.getSecondString().length();
        int sum = m + n;
        System.out.println("m+n: " + sum);
    }
}
