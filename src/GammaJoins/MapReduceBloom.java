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
public class MapReduceBloom extends ArrayConnectors {
    
     // input->HSplit->Bloom->HJoin->Merge, output: bitMap + merge result
    public Connector[] join(Connector input1, Connector input2, int joinkey1, int joinkey2) {
           
            Connector [] input1out = new Connector[splitLen];
	    Connector [] input2out = new Connector[splitLen];
            Connector [] result = new Connector[splitLen];
            Connector [] outMap1 = new Connector[splitLen];
            Connector [] output = new Connector[2];
            for (int i = 0; i < splitLen; ++i){
			input1out[i] = new Connector("HSplit1-" + i);
			input2out[i] = new Connector("HSplit2-" + i);
			result[i] = new Connector("pre-merge" + i);
		} 
            HSplit in1Split = new HSplit(input1, joinkey1, input1out);
	    HSplit in2Split = new HSplit(input2, joinkey2, input2out);
            
            for (int i = 0; i < splitLen; ++i){
                Connector outData1 = new Connector("Bloom1Data");
                outMap1[i] = new Connector("Bloom1BitMap"+i);
                Bloom bloom = new Bloom(input1out[i], outData1, outMap1[i], joinkey1);
                HJoin hjoin = new HJoin(outData1,input2out[i], joinkey1, joinkey2, result[i]);
            }
            output[0] = new Connector("merged");
            output[1] = new Connector("outputMap");
                
	    Merge m1 = new Merge(result, output[0]);
            MergeM m2 = new MergeM(outMap1, output[1]);
		
            return output;
    } 
}
