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
public class BFilter  extends Thread implements GammaConstants {
    ReadEnd inData;
    ReadEnd inMap;
    WriteEnd out;
    int joinKey;
    
    public BFilter (Connector inData, Connector inMap, Connector out, int joinKey) {	
	this.inData = inData.getReadEnd();
	this.inMap = inMap.getReadEnd();
	this.out = out.getWriteEnd();
	this.out.setRelation(inData.getRelation());
	this.joinKey = joinKey;
	ThreadList.add(this);	
	}
    
    public void run() {
        try {
            //get Map information
            String map = inMap.getNextString();
            BMap bitMap = BMap.makeBMap(map);
            //output field line
            Tuple in = inData.getNextTuple();
            out.putNextTuple(in);
            // output ---------line
            in = inData.getNextTuple();
            out.putNextTuple(in);
        while (true){
            in = inData.getNextTuple();
            if (in == null) {
                break;     
            }         
            if (bitMap.getValue(in.get(joinKey))) {
		out.putNextTuple(in);
            }
        }
        out.close();
        }catch (Exception e) {
             ReportError.msg(this.getClass().getName() + e);
        }
    }    
}
