/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gammaSupport;

/**
 *
 * @author dsb
 */
public interface GammaConstants {
    // Constants used in Gamma

    public static boolean debug = false;        // turn low-level debugging on
    public static int splitLen = 4;             // how many substreams SPLIT creates and MERGE merges
    public static boolean startNAdd = false;    // monitors addition and starting of threads -- useful! leave on!
    public static int mapSize = 7;              // number of hash bits
    public static String Rel = "RelationData/"; // directory in which to find base relations; used in ReadRelation
    
}
