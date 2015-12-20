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
public class HJoinRefineWithBloomFilters extends ArrayConnectors {
    // Hjoin + Bloom + BFilter
    public Connector join(Connector input1, Connector input2, int joinkey1, int joinkey2) throws Exception {
		Connector outData1 = new Connector("Bloom1Data");
                Connector outMap1 = new Connector("Bloom1BitMap");
                Bloom bloom = new Bloom(input1, outData1, outMap1, joinkey1);
                Connector out2 = new Connector("BFilter2Data");
                BFilter bfilter = new BFilter (input2, outMap1, out2, joinkey2);              
                Connector out = new Connector("out");
		HJoin hj = new HJoin(outData1,out2, joinkey1, joinkey2, out);
                return out;	
    }           
}
