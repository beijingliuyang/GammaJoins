/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GammaJoins;

import basicConnector.*;
import gammaSupport.*;
/**
 *
 * @author so
 */
public class Bloom extends Thread implements GammaConstants{
    private ReadEnd input;
    private WriteEnd outData;
    private WriteEnd outMap;
    private int joinkey;
    
    public Bloom (Connector input, Connector outData,Connector outMap, int joinkey) {
		
	this.input = input.getReadEnd();
	this.outData = outData.getWriteEnd();
	this.outData.setRelation(input.getRelation());
	this.outMap = outMap.getWriteEnd();
	this.outMap.setRelation(Relation.dummy);
	this.joinkey = joinkey;  // the field index of tuple
		
	ThreadList.add(this);
	}
    
    public void run() {
        BMap bitMap = BMap.makeBMap();
        try {
            //output field line
            Tuple in = input.getNextTuple();
            outData.putNextTuple(in);
            // output ---------line
            in = input.getNextTuple();
            outData.putNextTuple(in);
        while (true){
            in = input.getNextTuple();
            if (in == null) {
                break;     
            }
 //           check the content of hash of string  
 //           System.out.println(in.get(joinkey));
 //           System.out.println(BMap.myhash(in.get(joinkey)));
 //           System.out.println(Math.abs(in.get(joinkey).hashCode()));           
               outData.putNextTuple(in); 
               bitMap.setValue(in.get(joinkey), true);
        }
        outData.close();
        outMap.putNextString(bitMap.getBloomFilter());
        outMap.close();   
        }catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }   
}
