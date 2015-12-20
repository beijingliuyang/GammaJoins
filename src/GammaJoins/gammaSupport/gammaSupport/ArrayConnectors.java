/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gammaSupport;

import basicConnector.Connector;

/**
 *
 * @author Don
 */
public class ArrayConnectors  implements GammaConstants {
    
    // array constructors for connectors
    public static Connector[] initConnectorArray(String name) {
        Connector[] result = new Connector[splitLen];
        for (int i = 0; i < splitLen; i++) {
            result[i] = new Connector(name + "_" + i);
        }
        return result;
    }

    public static Connector[][] initConnectorMatrix(String name) {
        Connector[][] result = new Connector[splitLen][splitLen];
        for (int i = 0; i < splitLen; i++) for (int j=0; j<splitLen; j++) {
            result[i][j] = new Connector(name + "_" + i + "_" +j);
        }
        return result;
    }

    // this method converts a linear array of connectors (in) to a matrix of connectors (out)
    // used for cascading joins
    public static Connector[][] diagonalize (Connector[] in) {
        int i, j;
        Connector[][] out = new Connector[splitLen][splitLen];
        for (i=0; i<splitLen; i++) for (j=0; j<splitLen; j++) out[i][j]= null;
        for (i=0; i<splitLen; i++) out[i][i] = in[i];
        return out;
    }

    // transposes matrix into xirtam ('matrix' spelled backwards).
    // used for cascading joins
    public static Connector[][] transpose (Connector[][] matrix) {
        int i, j;

        Connector[][] xirtam = new Connector[splitLen][splitLen];
        for (i=0; i<splitLen; i++) for (j=0; j<splitLen; j++) xirtam[j][i] = matrix[i][j];
        
        return xirtam;
    }

}
