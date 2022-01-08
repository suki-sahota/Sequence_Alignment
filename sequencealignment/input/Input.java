package sequencealignment.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import sequencealignment.utils.Utilities;

public class Input {

    private static String firstStr = "";
    private static String secondStr = "";

    private static final String FILENAME = "Input.java";

    /* Reads input file and creates the two strings
     * that we will be working with during this program.
     * We save those two strings into the global variables
     * firstStr and secondStr to be used by rest of program.
     * input: input_file_path is the name of input file
     * return: none
     */
    public static String[] readInput(String input_file_path) {
        Utilities.averageMemoryUsage();

        BufferedReader br = createBufferedReader(input_file_path);
        buildStrings(br);
        closeBufferedReader(br);
        String finalStrings[] = new String[] { firstStr, secondStr };

        Utilities.averageMemoryUsage();
        return finalStrings;
    }

    private static BufferedReader createBufferedReader(String input_file_path) {
        File input_file = new File(input_file_path);
        FileReader input_file_reader = createFileReader(input_file);
        BufferedReader br = new BufferedReader(input_file_reader);
        return br;
    }

    private static FileReader createFileReader(File input_file) {
        FileReader input_file_reader = null;
        try {
            input_file_reader = new FileReader(input_file);
        } catch (IOException err) {
            System.err.println("An error occurred creating the file reader.");
            err.printStackTrace();
            System.exit(1);
        }
        return input_file_reader;
    }

    private static void closeBufferedReader(BufferedReader br) {
        try {
            br.close();
        } catch (IOException err) {
            System.err.println("An error occurred closing buffered reader.");
            err.printStackTrace();
            System.exit(1);
        }
    }

    private static void buildStrings(BufferedReader br) {
        String base_two = buildFirstString(br);
        buildSecondString(br, base_two);
    }

    private static String buildFirstString(BufferedReader br) {
        String base_one = readLineFromBufferedReader(br);
        StringBuilder firstCumulativeString = new StringBuilder(base_one);

        String base_two = 
            finishBuildingFirstString(br, base_one, firstCumulativeString);
        return base_two;
    }

    private static String readLineFromBufferedReader(BufferedReader br) {
        String line = "";
        try {
            line = br.readLine();
        } catch (IOException err) {
            System.err.println("Error occurred reading from buffered reader.");
            err.printStackTrace();
            System.exit(1);
        }
        return line;
    }

    private static String finishBuildingFirstString(BufferedReader br,
                                                    String base_one,
                                                    StringBuilder cumulative)
    {
        String str = "";
        int j = 0;

        while ((str = readLineFromBufferedReader(br)) != null) {
            if (!Character.isDigit(str.charAt(0))) { break; }

            ++j;
            int idx = Integer.parseInt(str);
            cumulative = generateInputString(cumulative, idx);
            Utilities.myAssert(
                validateInputString(cumulative.length(), j, base_one.length()),
                106,
                FILENAME
            );
        }

        Utilities.myAssert(0 < cumulative.length(), 111, FILENAME);
        firstStr = cumulative.toString(); // firstStr is done being built

        return str; // str contains base string for second string
    }

    private static void buildSecondString(BufferedReader br,
                                            String base_two)
    {
        StringBuilder secondCumulativeString = new StringBuilder(base_two);
        finishBuildingSecondString(br, base_two, secondCumulativeString);
    }

    private static void finishBuildingSecondString(BufferedReader br,
                                                   String base_two,
                                                   StringBuilder cumulative)
    {
        String str = "";
        int k = 0;

        while ((str = readLineFromBufferedReader(br)) != null) {
            Utilities.myAssert(Character.isDigit(str.charAt(0)), 132, FILENAME);

            ++k;
            int idx = Integer.parseInt(str);
            cumulative = generateInputString(cumulative, idx);
            Utilities.myAssert(
                validateInputString(cumulative.length(), k, base_two.length()),
                139,
                FILENAME
            );
        }
        Utilities.myAssert(0 < cumulative.length(), 143, FILENAME);
        secondStr = cumulative.toString(); // secondStr is done being built
    }

    /* Handles one iteration during the process
     * where we generate our cumulative string.
     * input: base StringBuilder where string is inserted during iteration
     * input: index where to insert base string
     * return: cumulative StringBuilder after one insertion
     */
    private static StringBuilder generateInputString(StringBuilder base,
                                                     int index)
    {
        // Check for valid insert index
        Utilities.myAssert(0 <= index && index < base.length(), 157, FILENAME);
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

    public static String getFirstString() {
        return firstStr;
    }

    public static String getSecondString() {
        return secondStr;
    }
}
