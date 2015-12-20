/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gammaSupport;

import java.util.Vector;

/**
 *
 * @author dsb
 */
public class Relation {
    // database is a list of relation (table) definitions
    static Vector<Relation> database = new Vector<Relation>();

    // used only in connectors that pass bitmaps
    public static Relation dummy = new Relation("Dummy", 0);


    String name;         // name of relation
    int size;            // number of fields in relation
    String fieldNames[]; // names of fields in relation
    int counter = 0;     // how many fieldnames have been entered

    public Relation(String name, int size) {
        this.name = name;
        this.size = size;
        fieldNames = new String[size];
        database.add(this);
    }

    public void addField(String fName) {
        fieldNames[counter++] = fName;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getSize() {
        return size;
    }

    public String getRelationName() {
        return name;
    }

    public String getField(int i) {
        return fieldNames[i];
    }

    public static Relation join(Relation r1, Relation r2, int joinkey1, int joinkey2) {
        Relation r = new Relation(r1.getRelationName() + "+" + r2.getRelationName(), r1.getSize() + r2.getSize() -1 );
        for (String f1 : r1.getFieldNames()) {
            r.addField(f1);
        }
        String jk = r2.fieldNames[joinkey2];
        for (String f2 : r2.getFieldNames()) {
            if (!f2.equals(jk))  // don't duplicate join key
            r.addField(f2);
        }
        database.add(r);
        return r;
    }
    
    // compare 2 relation definitions
    public boolean equals(Relation r) {
        if (r.size != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!fieldNames[i].equals(r.getField(i))) {
                return false;
            }
        }
        return true;
    }
}
