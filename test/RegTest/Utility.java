/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RegTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import static org.junit.Assert.*;

/**
 *
 * @author dsb
 */
public class Utility {

    public static int autopsy = -1;                // line number at which files differ      
    // constants

    private Utility() {
    } // should never be called

    // internal utility methods
    private static void sortFile(String inFileName, String outFileName, String[] eliminate) {
        BufferedReader br;
        LinkedList<String> lines = new LinkedList<>();
        String line;

        try {
            br = new BufferedReader(new FileReader(inFileName));
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                if (eliminate != null) {
                    for (String pattern : eliminate) {

                        line = line.replaceAll(pattern, "");
                    }
                }
                lines.add(line);
            }
            Collections.sort(lines);
            PrintStream ps = new PrintStream(new File(outFileName));
            for (String myline : lines) {
                ps.println(myline);
            }
            ps.close();
        } catch (Exception e) {
            throw new Error("unable to sort " + e.getMessage());
        }
    }

    private static void compareFiles(String fileA, String fileB, String fileMsg, String[] eliminate) {
        int lineNum = 0;
        String lineA = null, lineB = null;

        try {
            BufferedReader A = new BufferedReader(new FileReader(fileA));
            BufferedReader B = new BufferedReader(new FileReader(fileB));
            lineA = "";
            lineB = "";
            while (lineA.equals(lineB)) {
                lineNum++;
                lineA = A.readLine();
                lineB = B.readLine();
                if (lineA == null && lineB == null) {
                    return;
                }
                if ( (lineA == null && lineB != null) || (lineA != null && lineB == null) )
                    break;
                if (eliminate != null) {
                    for (String pattern : eliminate) {
                        lineA = lineA.replaceAll(pattern, "");
                        lineB = lineB.replaceAll(pattern, "");
                    }
                }
            }
            autopsy = lineNum;
        } catch (Exception e) {
            if (originalErr != null) {
                System.setErr(originalErr);
            }
            throw new Error("Regression Test: Unable to read file: " + e.getMessage());
        }
        System.err.println();
        System.err.println(" output>" + lineA);
        System.err.println("correct>" + lineB);
        System.err.flush();
        fail(fileMsg + " on line " + lineNum);
    }
// usage: suppose a program outputs files A and B and produces text to StdOut.
    // if this program changes, we want to know if it still produces the correct version of these files
    // and has the same StdOut text.  This can be accomplished by the calls:
    //    regTest.redirectStdOut("stdout.txt");  (place output in file "stdout.txt"
    //    {run program};
    //    regTest.validate("stdout.txt", "correctStdOut.txt", false);  (false means don't sort before comparing files
    //    regTest.validate("A.txt", "correctA.txt", true);   (true means sort both files before comparing)
    //    regTest.validate("B.txt", "correctB.txt", true);
    // sorting before comparison is needed when output ordering may be different and non-consequential
    private static PrintStream originalOut = null;
    private static PrintStream originalErr = null;
    private static PrintStream outfile = null;
    private static PrintStream errfile = null;

    public static void init() {
        originalOut = null;
        originalErr = null;
        outfile = null;
        errfile = null;
    }

    public static void redirectStdOut(String outputFile) {
        originalOut = System.out;
        try {
            outfile = new PrintStream(outputFile);
            System.setOut(outfile);
        } catch (Exception e) {
            throw new Error("RedirectStdOut failed to initialize");
        }
    }

    public static void redirectStdErr(String outputFile) {
        originalErr = System.err;
        try {
            errfile = new PrintStream(outputFile);
            System.setErr(errfile);
        } catch (Exception e) {
            throw new Error("RedirectStdErr failed to initialize");
        }
    }
    private final static String sortedOut = "sortedOut.txt";
    private final static String sortedCorrect = "sortedCor.txt";

    private static void done() {
        if (outfile != null) {
            outfile.flush();
            outfile.close();
            outfile = null;
        }
        if (errfile != null) {
            errfile.flush();
            errfile.close();
            errfile = null;
        }
        if (originalOut != null) {
            System.out.flush();
            System.out.close();
            System.setOut(originalOut);
            originalOut = null;
        }
        if (originalErr != null) {
            System.err.flush();
            System.err.close();
            System.setErr(originalErr);
            originalErr = null;
        }
    }

    public static void validate(String outputFile, String correctFile, boolean sortedTest) {
        validate(outputFile, correctFile, sortedTest, null);
    }

    public static void validate(String outputFile, String correctFile, boolean sortedTest, String[] eliminate) {

        String fileMsg = outputFile + " differs from " + correctFile;

        if (sortedTest) {
            sortFile(outputFile, sortedOut, eliminate);
            sortFile(correctFile, sortedCorrect, eliminate);
            outputFile = sortedOut;
            correctFile = sortedCorrect;
            fileMsg = fileMsg + "(compare sortedOut.txt with sortedCor.txt)";
        }
        compareFiles(outputFile, correctFile, fileMsg, eliminate);
    }

    public static void validate(String outputFile, boolean sortedTest) {
        validate(outputFile, "Correct/" + outputFile, sortedTest);
    }

    public static void validate(String outputFile, boolean sortedTest, String[] eliminate) {
        validate(outputFile, "Correct/" + outputFile, sortedTest, eliminate);
    }
}
