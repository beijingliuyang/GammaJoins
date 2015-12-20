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
public class HSplit extends Thread implements GammaConstants {
    private ReadEnd in;
    private WriteEnd[] out = new WriteEnd [splitLen];
    int joinkey;
    
    public HSplit(Connector in, int joinkey, Connector[] out ){
        this.in = in.getReadEnd();
	this.joinkey = joinkey;
	Relation r = in.getRelation();
        assert( out.length == splitLen);
        for (int i=0; i<splitLen; i++) {
            this.out[i]=out[i].getWriteEnd();
            out[i].setRelation(r);
        }
	ThreadList.add(this);
    }
    
    public void run() {
    try {
        //output field line
        Tuple input = in.getNextTuple();
        for (int i=0; i<splitLen; i++){
                out[i].putNextTuple(input);
          } 
        // output ---------line
        input = in.getNextTuple();
        for (int i=0; i<splitLen; i++){
                out[i].putNextTuple(input);
          } 
        //hashsplit
        while (true) {           
                input = in.getNextTuple();
                if (input == null) {
                    break;     
                }
		int index = BMap.myhash(input.get(joinkey));
                out[index].putNextTuple(input);
        }
        for (int i=0; i<splitLen; i++){
           out[i].close();
        } 
        } catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }
    
}
