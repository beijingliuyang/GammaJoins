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
public class Gamma extends ArrayConnectors {
   // input->HSplit->Bloom+BFilter->HJoin->Merge
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
                Connector outData1 = new Connector("Bloom1Data");
                Connector outMap1 = new Connector("Bloom1BitMap");
                Bloom bloom = new Bloom(input1out[i], outData1, outMap1, joinkey1);
                Connector out2 = new Connector("BFilter2Data");
                BFilter bfilter = new BFilter (input2out[i], outMap1, out2, joinkey2);
                HJoin hjoin = new HJoin(outData1,out2, joinkey1, joinkey2, result[i]);
            }
            Connector merge = new Connector("merged");
                
	    Merge m = new Merge(result, merge);
		
            return merge;	
    }  
}
