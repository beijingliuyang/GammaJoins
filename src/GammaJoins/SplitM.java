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
public class SplitM extends Thread implements GammaConstants{
    private ReadEnd in;
    private WriteEnd [] out = new WriteEnd [splitLen];
    
    public SplitM(Connector in, Connector [] out ){
        this.in = in.getReadEnd();
	Relation r = in.getRelation();
        assert( out.length == splitLen);
        for (int i=0; i<splitLen; i++) {
            this.out[i]=(out[i].getWriteEnd());
            out[i].setRelation(r);
        }
	ThreadList.add(this);
    }
    
    public void run() {
    try {
        String map = in.getNextString();
        assert(map.length()==splitLen*mapSize);
        BMap bitMap = BMap.makeBMap(map);
        for (int i=0; i<splitLen; i++){
             String s = bitMap.mask(map,i);
             out[i].putNextString(s);             
           }
        for (int i=0; i<splitLen; i++){
           out[i].close();
        } 
        } catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }  
}
