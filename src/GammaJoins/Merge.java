/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GammaJoins;

import basicConnector.*;
import gammaSupport.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author so
 */
public class Merge extends Thread implements GammaConstants {
    
    private ReadEnd[] in = new ReadEnd [splitLen];
    private WriteEnd out;
//    int hashkey;
    
    public Merge (Connector[] in, Connector out) {
        Relation r = null;
        assert( in.length == GammaConstants.splitLen );
        for (int i=0; i<GammaConstants.splitLen; i++) {
            this.in[i] = in[i].getReadEnd();
        }
        r = in[0].getRelation();
        this.out = out.getWriteEnd();
        out.setRelation(r);
	ThreadList.add(this);
	}
    
    public void run() {
    try {
        //output field line
        Tuple input = in[0].getNextTuple();
        out.putNextTuple(input);
        // output ---------line
        input = in[0].getNextTuple();
        out.putNextTuple(input);
        for (int i=1; i<GammaConstants.splitLen; i++){
            input = in[i].getNextTuple();
            input = in[i].getNextTuple();
         } 
        //merge
        for (int i=0; i<GammaConstants.splitLen; i++) {
            while (true){
            input = in[i].getNextTuple();
            if (input == null) {
                break;     
            }
            out.putNextTuple(input);
            }        
        }
        out.close();        
    }
    catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }  
}
