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
public class HJoinRefineWithParallelBloom_ParallelBF_Parallel_Join extends ArrayConnectors  {
	// join: multi-input-Bloom+BFilter->HJoin->Merge
	public Connector[] join(Connector[] input1, Connector input2[], int joinkey1, int joinkey2) throws Exception {
                assert (input1.length==splitLen);
                assert (input2.length==splitLen);
                Connector [] hjoin = new Connector[splitLen];
		
		for (int i = 0; i < splitLen; i++) {
                    Connector outData1 = new Connector("Bloom1Data");
                    Connector outMap1 = new Connector("Bloom1BitMap");
                    Bloom bloom = new Bloom(input1[i], outData1, outMap1, joinkey1);
                    Connector out2 = new Connector("BFilter2Data");
                    BFilter bfilter = new BFilter (input2[i], outMap1, out2, joinkey2);              
                    hjoin[i] = new Connector("out" + i);
		    HJoin hj = new HJoin(outData1,out2, joinkey1, joinkey2, hjoin[i]);
		}
                return hjoin;	
        }
	
}
