package sequencealignment.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.Math;

import sequencealignment.basic.Basic;
import sequencealignment.efficient.Efficient;
import sequencealignment.input.Input;
import sequencealignment.symbol.Symbol;
import sequencealignment.utils.Utilities;

public class Output {

    private static int m = -1;
    private static int n = -1;

    // True when called by efficient algorithm
    private static boolean isEfficient = false;
    private static final String FILENAME = "Output.java";

    /* Writes aligned strings, score, memory, and time to the 
     * "output.txt" file. Output will be graded by instructors.
     * input: alignedStrings is an array with two aligned strings
     * input: code is 0 when called by basic algo and 1 when called by efficent
     * return: none
     */
    public static void writeOutput(String[] alignedStrings, int code) {
        Utilities.averageMemoryUsage();

        if (code == 1) { isEfficient = true; }

        FileWriter myWriter = createFileWriter();
        writeToOutput(alignedStrings, myWriter);
        closeFileWriter(myWriter);

        Utilities.averageMemoryUsage();
    }

    private static void validateStrings(String[] alignedStrings) {
        Utilities.myAssert(alignedStrings.length == 2, 43, FILENAME);
        m = Input.getFirstString().length();
        n = Input.getSecondString().length();

        Utilities.myAssert(
            Math.max(m, n) <= alignedStrings[0].length()
                && alignedStrings[0].length() <= m + n,
            50,
            FILENAME
        );
        Utilities.myAssert(
            alignedStrings[0].length() == alignedStrings[1].length(),
            55, 
            FILENAME
        );
    }

    private static FileWriter createFileWriter() {
        File myObj = new File(Utilities.OUTPUT_FILENAME); // Creates new file

        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(myObj);
        } catch (IOException err) {
            System.err.println("An error occurred creating the File Writer.");
            err.printStackTrace();
            System.exit(1);
        }
        return myWriter;
    }

    private static void writeToOutput(String[] alignedStrings,
                                      FileWriter myWriter)
    {
        writeStrings(alignedStrings, myWriter);

        writeScore(myWriter);

        writeTime(myWriter);

        writeMemory(myWriter);
    }

    /* Writes first and last 50 characters to output file.
     * If a string is less than 50 characters, it will write
     * the entire string to output file.
     * input: alignedStrings is an array of two aligned strings
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeStrings(String[] alignedStrings,
                                     FileWriter myWriter)
    {
        writeAlignedStr(alignedStrings[0], myWriter);
        writeAlignedStr(alignedStrings[1], myWriter);
    }

    private static void writeAlignedStr(String str, FileWriter myWriter) {
        // Write first 50 characters of string
        write50Chars(str.substring(0, Utilities.MAX_LEN), myWriter);
        writeToFile(" ", myWriter);

        // Write last 50 characters of string
        write50Chars(
            str.substring(str.length() - Utilities.MAX_LEN),
            myWriter
        );
        writeToFile("\n", myWriter);
    }

    private static void write50Chars(String chars, FileWriter myWriter) {
        writeToFile(chars, myWriter);
    }

    private static void writeToFile(String str, FileWriter myWriter) {
        try {
            myWriter.write(str);
        } catch (IOException err) {
            System.err.println("An error occurred writing to file.");
            err.printStackTrace();
            System.exit(1);
        }
    }

    /* Writes score to the output file.
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeScore(FileWriter myWriter) {
        int myScore = -1;

        if (isEfficient == true) {
            myScore = Efficient.getScore();
        } else {
            myScore = Basic.getScore();
        }
        writeToFile(String.valueOf(myScore), myWriter);
        writeToFile("\n", myWriter);

        System.out.println("Cost: " + String.valueOf(myScore));
    }

    /* Writes time used to the output file in seconds.
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeTime(FileWriter myWriter) {
        double elapsedTime = findElapsedTime();
        writeToFile(String.valueOf(elapsedTime), myWriter);
        writeToFile("\n", myWriter);

        System.out.println("Time: " + String.valueOf(elapsedTime));
    }

    private static double findElapsedTime() {
        long endTime = System.currentTimeMillis();

        // Calculate elapsed time in milliseconds
        double elapsedTime = endTime - Utilities.getStartTime();
        elapsedTime /= Utilities.THOUSAND; // Convert to seconds
        return elapsedTime;
    }

    /* Writes memory used to the output file in KiloBytes.
     * input: myWriter is FileWriter to write in output file
     * return: none
     */
    private static void writeMemory(FileWriter myWriter) {
        convertMemoryToKB();
        writeToFile(String.valueOf(Utilities.memory), myWriter);
        writeToFile("\n", myWriter);

        System.out.println(
            "Memory: " + String.valueOf(Utilities.memory) + "\n"
        );
    }

    private static void convertMemoryToKB() {
        Utilities.memory /= Utilities.KILO; // convert to KiloBytes
    }

    private static void closeFileWriter(FileWriter myWriter) {
        flushFileWriter(myWriter);
        try {
            myWriter.close();
        } catch (IOException err) {
            System.err.println("An error occurred closing the file writer.");
            err.printStackTrace();
            System.exit(1);
        }
    }

    private static void flushFileWriter(FileWriter myWriter) {
        try {
            myWriter.flush();
        } catch (IOException err) {
            System.err.println("An error occurred flushing the file writer.");
            err.printStackTrace();
            System.exit(1);
        }
    }
}
