/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GammaJoins;

import basicConnector.*;
import gammaSupport.*;
import java.io.*;
import java.util.StringTokenizer;

public class ReadRelation extends Thread implements GammaConstants {
    private BufferedReader input;
    private Relation r;
    private WriteEnd w;


    public ReadRelation( String relationName, Connector out ) throws Exception {
        this(relationName+".txt", relationName, out);
    }

    public ReadRelation(String filename, String relationName, Connector out) throws Exception {
        // Step 0: remember write end
        w = out.getWriteEnd();
        // Step 1:  open data file and create relation
        input = new BufferedReader(new FileReader(filename));
        String line = input.readLine();
        r = createRelation(line, relationName);
        out.setRelation(r);
        w.putNextTuple(Tuple.makeTupleFromFileData(r, line));

        // Step 2: skip ---- line
        line = input.readLine();    
        w.putNextTuple(Tuple.makeTupleFromFileData(r, line));    
        // Step 3: ready to read data      
        ThreadList.add(this);
    }

    // this static method creates a Relation object whose name is "relationName"
    // from the first line read in a data file.
    static  Relation createRelation( String line, String relationName ) {

        // Step 1: count the number of fields (attributes) in relation
         StringTokenizer st = new StringTokenizer(line);
         int size = st.countTokens();  
        // Step 2: create relation
         Relation r = new Relation( relationName, size );
        // Step 3: populate fieldnames (attribute names)
         while (st.hasMoreTokens())
            r.addField(st.nextToken());
         return r;
    }

    @Override
    public void run() { 
        try {
        while(true) {
            Tuple t = getNextTuple();
            if (t==null) {
               
               break;
            }
             //do not output e.g. 5#null#null#null#null#null#
            int number = t.toString().charAt(0)-48;
            String temp=number+"#";
               for (int i=0; i<number; i++){
                    temp = temp+"null#";
                  }
            if (!t.toString().equals(temp)){
                w.putNextTuple(t);
                }      
        }
         w.close();
        } catch(Exception e) { ReportError.msg(this, e); }
    }

    public Tuple getNextTuple() throws Exception {       
        String line;
        while((line = input.readLine()) != null){
            return Tuple.makeTupleFromFileData(r, line);
        }
        return null;
   }
}
