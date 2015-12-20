/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GammaJoins;


import static GammaJoins.ReadRelation.createRelation;
import basicConnector.*;
import gammaSupport.*;
import java.io.BufferedReader;
import java.io.FileReader;
/**
 *
 * @author so
 */
public class BloomSimulator extends Thread implements GammaConstants{
    private BufferedReader input;
    private WriteEnd outMap;
    private Relation r;
    int joinKey;
    
    public BloomSimulator( String relationName, Connector outMap, int joinKey) throws Exception {
        this(relationName+".txt", relationName, outMap, joinKey);
    }
    
    public BloomSimulator(String filename, String relationName, Connector outMap, int joinKey) throws Exception {
        this.outMap = outMap.getWriteEnd();
        this.outMap.setRelation(Relation.dummy);
	input = new BufferedReader(new FileReader(filename));
        String line = input.readLine();
        r = createRelation(line, relationName);
        line = input.readLine();   // pass ----- line
        this.joinKey = joinKey;
        ThreadList.add(this);
	}
 
    public void run() {
        BMap bitMap = BMap.makeBMap();
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
                bitMap.setValue(t.get(joinKey), true);;
                }      
        }
         outMap.putNextString(bitMap.getBloomFilter());     
         outMap.close();
        } catch (Exception e) {
             ReportError.msg(this.getClass().getName() + e);
        }   
    } 
    
     public Tuple getNextTuple() throws Exception {       
        String line;
        while((line = input.readLine()) != null){
            return Tuple.makeTupleFromFileData(r, line);
        }
        return null;
   } 
}
