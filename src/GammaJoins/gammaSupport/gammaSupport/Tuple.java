/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gammaSupport;

import java.util.*;

/**
 *
 * @author dsb
 */
public class Tuple {
    // serialized tuples have their fields separated by this character
    private static String separator="#";

    // a tuple is an array[size] of Strings
    private String[] field;
    private int size;

    public Tuple(int size) {
        field = new String[size];
        this.size = size;
    }

    // field getters and setters
    public void set(int fieldNumber, String value ) { field[fieldNumber] = value; }
    public String get(int fieldNumber) { return field[fieldNumber]; }
    public int getSize() { return size; }
    public String[] getFields() { return field; }

    // this method serializes a tuple into a string
    // note that the tuple begins with its size (in fields)
    @Override
    public String toString() {
        String result = size + separator;
        for (String i : field) result = result + i + separator;
        return result;
    }

    // this constructor unserializes a string into a tuple
    static public Tuple makeTupleFromFileData(Relation r, String line) {
        StringTokenizer st = new StringTokenizer(line);
        int size = r.getSize();
        Tuple t = new Tuple(size);
        int fieldNumber = 0;
        while (st.hasMoreTokens())
            t.set(fieldNumber++, st.nextToken());
        return t;
     }

    // this constructor unserializes a string into a tuple
    static public Tuple makeTupleFromPipeData(String line) {
        StringTokenizer st = new StringTokenizer(line, separator, false);
        int size = Integer.parseInt(st.nextToken());
        Tuple t = new Tuple(size);
        int fieldNumber = 0;
        while (st.hasMoreTokens())
            t.set(fieldNumber++, st.nextToken());
        return t;
     }

    public void println(Relation r) {
        String[] fieldNames = r.getFieldNames();
        for (int i=0; i<size; i++) {
            System.out.println(fieldNames[i] + " = " + field[i]);
        }
        System.out.println();
    }

    public static Tuple join( Tuple t1, Tuple t2, int joinkey1, int joinkey2 ) {
        Tuple t = new Tuple(t1.getSize() + t2.getSize()-1);
        int fieldNumber = 0;
        for (String f1 : t1.getFields()) t.set(fieldNumber++, f1);
        for (int i=0; i<t2.getSize();i++){
           if (i!=joinkey2) {t.set(fieldNumber++, t2.get(i));}
        }
     //   for (String f2 : t2.getFields()) if (!t2.field[joinkey2].equals(f2)) t.set(fieldNumber++, f2);
        return t;
    }
}
