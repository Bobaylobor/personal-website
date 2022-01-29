/*=============================================================================
 |   Assignment: Program #1a: Creating and Ternary–Searching a Binary File
 |       Author: Baylor Warrick (baylorwarrick@email.arizona.edu)
 |       Grader: I don't know how to find out this information
 |
 |       Course: CSC460
 |   Instructor: L. McCann
 |     Due Date: 1/26/22 12:30pm
 |
 |  Description: Usage:
 |                    Prog1A file.csv
 |               Reads in a csv file which has the following format:
 |                    -The first line is the column names
 |                    -Each subsequent line is a record with 9 string fields and
 |                        4 integer fields
 |                    -Fields are separate by commas. If a field is empty, there
 |                        are two commas
 |                    -If a field has a comma as part of its string, the entire
 |                        field is surrounded with quotation marks
 |               The program reads through this and stores it as a binary file
 |               with the same basename, so if the file is file.csv, the output
 |               will be file.bin. Each record is made the same size by padding
 |               the strings to match the largest string in that respective
 |               column. The 9 string maxes (integers) are stored at the
 |               beginning of the binary file, and then right after that is all
 |               the records.
 |
 |     Language: Java 16
 | Ex. Packages: None
 |
 | Deficiencies: I know of no deficiencies in the code.
 *===========================================================================*/

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Scanner;

/*+----------------------------------------------------------------------
 ||
 || Class Record
 ||
 ||        Author: Baylor Warrick
 ||
 ||       Purpose: Represents a record of the offsets database. Holds an array
 ||                of 9 string fields and an array of 4 int fields. Has methods
 ||                for dumping itself to a binary file and comparing to another
 ||                record based on the first integer of the int array.
 ||
 || Inherits From: None
 ||
 ||    Interfaces: None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||     Constants: None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||  Constructors: public Record(String line)
 ||
 || Class Methods: None
 ||
 || Inst. Methods:
 ||          public void dumpRecord(RandomAccessFile binFile, int[] stringMaxes)
 ||              throws IOException
 ||          public int getTotalCreditsIssued()
 ||          public int compareTo(Record o)
 ||
 ++-----------------------------------------------------------------------*/
class Record implements Comparable<Record> {

    String[] stringFields = new String[Prog1A.N_STRING_FIELDS];
    int[] intFields = new int[Prog1A.N_INT_FIELDS];

    /*---------------------------------------------------------------------
    | Constructor Record(line)
    |
    | Purpose: Constructs a Record object from a line from the csv file,
    |     Populating the 9 string fields array and the 4 int fields array
    |      accordingly.
    |
    | Pre-condition: Assumes the line has 13 fields separated by commas, and
    |     that the first 9 fields are string (within quotes if there is a
    |     comma within the string) and the last 4 are integers (within
    |     quotes if there are commas in the number). Assumes there is no
    |     new line at the end of the line and that all integers have only
    |     digits in them.
    |
    | Post-condition: The stringFields and intFields fields will be populated
    |     with the respective values from the line passed in.
    |
    | Parameters:
    |     line -- a line from the csv file with 13 comma separated fields, no
    |         new line at the end.
    *-------------------------------------------------------------------*/
    public Record(String line) {
        String[] fields = Prog1AUtils.splitLineIntoFields(line);

        for (int i = 0; i < Prog1A.N_STRING_FIELDS; i++) {
            String asciiString = Normalizer.normalize(fields[i], Normalizer.Form.NFKD)
                    .replaceAll("[^\\p{ASCII}]", "");
            stringFields[i] = asciiString;
        }
        for (int i = 0; i < Prog1A.N_INT_FIELDS; i++) {
            intFields[i] = Prog1AUtils.parseCommaSeparatedInt(fields[i + Prog1A.N_STRING_FIELDS]);
        }
    }

    /*---------------------------------------------------------------------
    | Method dumpRecord
    |
    | Purpose: Writes this record to the binFile by writing the 9 string fields
    |     and then the 4 ints. To make sure this record is the same size as
    |     the others in the binFile, it uses stringMaxes which contains 9
    |     integers and pads each string according to the respective stringMax
    |     with spaces on the right of the string.
    |
    | Pre-condition: Assumes the binFile pointer is at the end of the file.
    |     Assumes stringMaxes has 9 ints, one for each respective max string
    |     length, and also assumes that each stringMax is longer than the
    |     length of the respective string in this record.
    |
    | Post-condition: The information for this record will be in the binFile,
    |     with the binFile pointer being moved past the record.
    |
    | Parameters:
    |     binFile -- binary file, ready for another record to be written to it,
    |         left off at the last record
    |     stringMaxes -- array of 9 integers, each representing the amount
    |         which to pad the respective string field of the record
    |
    | Returns: None.
    *-------------------------------------------------------------------*/
    public void dumpRecord(RandomAccessFile binFile, int[] stringMaxes)
            throws IOException {
        for (int i = 0; i < Prog1A.N_STRING_FIELDS; i++) {
            // pads the string
            String field = Prog1AUtils.formatString(stringFields[i], stringMaxes[i]);
            binFile.writeBytes(field);
        }
        for (int i = 0; i < Prog1A.N_INT_FIELDS; i++) {
            binFile.writeInt(intFields[i]);
        }
    }

    /*---------------------------------------------------------------------
    |  Method getTotalCreditsIssued
    |
    |  Purpose: Retrieves the total credits issued value from this record. Used
    |      for comparing with other records.
    |
    |  Returns: The first integer field which is the totalCreditsIssued
    *-------------------------------------------------------------------*/
    public int getTotalCreditsIssued() {
        return intFields[0];
    }

    /*---------------------------------------------------------------------
    | Method compareTo
    |
    | Purpose: Compares to another record by comparing the first integer field
    |     of each, the total credits issued. Used for sorting records.
    |
    | Pre-condition: The record o being passed in is a valid record with its
    |     fields already populated.
    |
    | Post-condition: Nothing is changed.
    |
    | Parameters:
    |     o -- other record.
    |
    | Returns: Negative if this record has less total credits issued than other
    |     record, 0 if same number of records, positive if more credits than
    |     other record.
    *-------------------------------------------------------------------*/
    @Override
    public int compareTo(Record o) {
        return getTotalCreditsIssued() - o.getTotalCreditsIssued();
    }
}

/*+----------------------------------------------------------------------
 ||
 || Class Prog1A
 ||
 ||        Author: Baylor Warrick
 ||
 ||       Purpose: Runs the main functionality of part A of Project 1. Reads in
 ||           a csv file and creates Record objects. Sorts the records by
 ||           total credits issued (10th field of each row/first integer field),
 ||           Then writes the records in order into a binary file with the same
 ||           base name as the csv file passed in as a command line argument.
 ||           It will guarantee that each record is the same size by padding the
 ||           string fields to the maximum length string. It also stores the
 ||           stringMaxes (the maximum length for each of the 9 string fields;
 ||           each as an integer) at the beginning of the file so that when
 ||           reading the file, we know how long each field is.
 ||
 || Inherits From: None
 ||
 ||    Interfaces: None
 ||
 |+-----------------------------------------------------------------------
 ||
 ||     Constants: N_STRING_FIELDS = 9
 ||                    How many string fields there are on each line of csv file
 ||                N_INT_FIELDS = 4
 ||                    How many int field there are on each line of csv file
 ||
 |+-----------------------------------------------------------------------
 ||
 ||  Constructors: None
 ||
 || Class Methods:
 ||          public static void main(String[] args)
 ||          public static void loadRecords(Scanner sc, Record[] records)
 ||
 || Inst. Methods: None
 ||
 ++-----------------------------------------------------------------------*/
public class Prog1A {

    public static final int N_STRING_FIELDS = 9;  // the first 9 fields are strings
    public static final int N_INT_FIELDS = 4;  // the next 4 fields are ints

    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            Scanner sc = new Scanner(file);
            int[] stringMaxes = new int[N_STRING_FIELDS];
            int numRecords = Prog1AUtils.findStringMaxes(sc, stringMaxes);

            Record[] records = new Record[numRecords];
            sc = new Scanner(file); // back to beginning of the file
            sc.nextLine(); // skip column names line
            loadRecords(sc, records); // populate records array with all the Records
            Arrays.sort(records); // sorts by Total Credits Issued

            // create binary file with same basename as file passed in
            Path path = Paths.get(args[0]);
            String baseName = path.getFileName().toString().split("\\.")[0];
            RandomAccessFile binFile = new RandomAccessFile(baseName + ".bin", "rw");

            // write the 9 string maxes at the start of the binary file
            for (int i = 0; i < N_STRING_FIELDS; i++) {
                binFile.writeInt(stringMaxes[i]);
            }

            // write records to binary file
            for (int i = 0; i < numRecords; i++) {
                records[i].dumpRecord(binFile, stringMaxes);
            }

            sc.close();
            binFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /*---------------------------------------------------------------------
    | Method loadRecords
    |
    | Purpose: Uses the scanner object sc to read through csv file. For each
    |     line, it creates a Record using the class Record constructor, and
    |     stores it in the records array until the array is full.
    |
    | Pre-condition: The records array is assumed to be empty, but to be the
    |     exact size to fit all the records in the file.
    |
    | Post-condition: records array is filled with a Record object for every
    |     record in the file. Scanner sc is at the end of the file now.
    |
    | Parameters:
    |     sc -- scanner object attached to the csv file, placed at the beginning
    |         of the file
    |     records -- empty array with just enough slots to fit all records
    |
    | Returns: None.
    *-------------------------------------------------------------------*/
    public static void loadRecords(Scanner sc, Record[] records) {
        int index = 0;
        while (sc.hasNextLine()) {
            records[index] = new Record(sc.nextLine());
            index++;
        }
    }

}
