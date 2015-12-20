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
public class MergeM extends Thread implements GammaConstants{
    private WriteEnd out;
    private ReadEnd [] in = new ReadEnd[splitLen];
    private Relation r;
    
    public MergeM(Connector[] in, Connector out ){
        for (int i=0; i<splitLen; i++) {
            this.in[i]=in[i].getReadEnd();
        }
        Relation r = in[0].getRelation();
        this.out = out.getWriteEnd();
        out.setRelation(r);
       
	ThreadList.add(this);
    }
    
    public void run() {
    try {
        BMap bitMap = BMap.makeBMap();
        for (int i=0; i<splitLen; i++){
            String s = in[i].getNextString();
            BMap temp = BMap.makeBMap(s);
            bitMap = BMap.or(bitMap, temp);          
           }
        out.putNextString(bitMap.getBloomFilter());
        out.close();
        } catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }  
}
