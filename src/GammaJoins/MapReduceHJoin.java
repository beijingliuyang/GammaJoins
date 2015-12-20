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
public class MapReduceHJoin extends ArrayConnectors {
    // input->Bloom+BFilter->HJoin
    public Connector join(Connector input1, Connector input2, int joinkey1, int joinkey2) {
          
            Connector [] input1out = new Connector[splitLen];
	    Connector [] input2out = new Connector[splitLen];
            Connector [] result = new Connector[splitLen];
            for (int i = 0; i < splitLen; ++i){
			input1out[i] = new Connector("HSplit1-" + i);
			input2out[i] = new Connector("HSplit2-" + i);
			result[i] = new Connector("pre-merge" + i);
		} 
            HSplit in1Split = new HSplit(input1, joinkey1, input1out);
	    HSplit in2Split = new HSplit(input2, joinkey2, input2out);
            
            for (int i = 0; i < splitLen; ++i){
                HJoin hjoin = new HJoin(input1out[i],input2out[i], joinkey1, joinkey2, result[i]);
            }
            Connector merge = new Connector("merged");
                
	    Merge m = new Merge(result, merge);
		
            return merge;	
    }  
}
