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
public class MapReduceBFilter extends ArrayConnectors {
    // input->HSplit->BFilter->HJoin->Merge, BitMap from input
    public Connector join(Connector input1, Connector input2, Connector bitMap, int joinkey1, int joinkey2) {
           
                Connector [] input1out = new Connector[splitLen];
		Connector [] input2out = new Connector[splitLen];
                Connector [] hjoin = new Connector[splitLen];
                Connector [] map = new Connector[splitLen];
		
		for (int i = 0; i < splitLen; ++i){
			input1out[i] = new Connector("HSplit1-" + i);
			input2out[i] = new Connector("HSplit2-" + i);
			hjoin[i] = new Connector("HSplit1+HSplit2-" + i);
                        map[i] = new Connector("map-"+i);
		} 
		
		HSplit in1Split = new HSplit(input1, joinkey1, input1out);
		HSplit in2Split = new HSplit(input2, joinkey2, input2out);
		HJoin [] hjoins = new HJoin[splitLen];
                SplitM sm = new SplitM (bitMap, map);
		
		for (int i = 0; i < splitLen; i++) {
                    Connector out2 = new Connector("Data2afterBFilter");
                    BFilter bfilter = new BFilter (input2out[i], map[i], out2, joinkey2);
		    hjoins[i] = new HJoin(input1out[i],out2, joinkey1, joinkey2, hjoin[i]);
		}
		
		Connector merge = new Connector("merged");
                
		Merge m = new Merge(hjoin, merge);
		
                return merge;	
    }  
    
}
