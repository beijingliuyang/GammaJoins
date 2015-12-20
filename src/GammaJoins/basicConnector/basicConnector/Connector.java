/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicConnector;

import gammaSupport.Relation;
import gammaSupport.ReportError;
import java.io.*;
import java.util.LinkedList;

/**
 *
 * @author Don
 */
public class Connector {

    static LinkedList<Connector> all = new LinkedList<Connector>();
    public BufferedReader in;
    public PrintStream out;
    public Relation r;
    public String name;

    // constructors
    private Connector() { /* should never be called */ }

    public Connector(String name) {
        try {
            this.name = name;
            PipedOutputStream writeEnd = new PipedOutputStream();
            PipedInputStream readEnd = new PipedInputStream(writeEnd);
            out = new PrintStream(writeEnd);
            in = new BufferedReader(new InputStreamReader(readEnd));
            all.add(this);
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + e);
        }
    }

   // instance methods
    public ReadEnd getReadEnd() {
        return new ReadEnd(this, r);
    }

    public WriteEnd getWriteEnd() {
        return new WriteEnd(this);
    }

    public Relation getRelation() {
        return r;
    }

    public void setRelation(Relation r) {
        this.r = r;
    }

    // used for debugging
    public static void verifyRelationSet() {
        for (Connector c : all) {
            c.verifyRelation();
        }
    }

    // every connector must be associated with some relation (to type
    // the tuples that flow along it)
    public void verifyRelation() {
        if (r == null) {
            ReportError.msg("You forgot to set the relation field in connector " + name);
            System.exit(1);
        }
    }

    public String getName() { return name; }
}
