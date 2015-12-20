/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GammaJoinsTest;

import GammaJoins.*;
import basicConnector.*;
import gammaSupport.*;
import org.junit.Test;

/**
 *
 * @author so
 */
public class MainTest {

    public MainTest() {
        RegTest.Utility.init();
    }
        public void print(String rname) throws Exception {
        System.out.println("Printing: " + rname);
        ThreadList.init();
        Connector c1 = new Connector("input");
        ReadRelation r1 = new ReadRelation(rname, c1);
        Print p = new Print(c1);
        ThreadList.run(p);
    }
    @Test
    public void printtest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        print("client");
        print("viewing");
        print("orders");
        print("parts");
        print("odetails");
        RegTest.Utility.validate("out.txt", "Correct/printtest.txt", false);
    } 
    public void sink(String rname) throws Exception {
        System.out.println("Sinking: " + rname);
        ThreadList.init();
        Connector c1 = new Connector("input");
        ReadRelation r1 = new ReadRelation(rname, c1);
        Sink p = new Sink(c1);
        ThreadList.run(p);
    }
    @Test
    public void sinktest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        sink("client");
        sink("viewing");
        sink("orders");
        sink("parts");
        sink("odetails");
        RegTest.Utility.validate("out.txt", "Correct/sinktest.txt", false);
    }
    public void donothing(String rname) throws Exception {
        System.out.println("Donothing: " + rname);
        ThreadList.init();
        Connector in = new Connector("input");
        ReadRelation r = new ReadRelation(rname, in);
        Connector out = new Connector("output");
        DoNothing d = new DoNothing(in, out);
        Print p = new Print(out);
        ThreadList.run(p);
    }
    @Test
    public void donothingtest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        donothing("client");
        donothing("viewing");
        donothing("orders");
        donothing("parts");
        donothing("odetails");
        RegTest.Utility.validate("out.txt", "Correct/donothingtest.txt", false);
    }
    public void join(String r1name, String r2name, int jk1, int jk2) throws Exception {
        System.out.println("Joining " + r1name + " with " + r2name);
        ThreadList.init();
        Connector c1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(r1name, c1);
        Connector c2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(r2name, c2);
        Connector o = new Connector("output");
        HJoin hj = new HJoin(c1, c2, jk1, jk2, o);
        Print p = new Print(o);
        ThreadList.run(p);
    }
    @Test
    public void jointest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        join("parts", "odetails", 0, 1);
        join("client", "viewing", 0, 0);
        join("orders", "odetails", 0, 0);
        RegTest.Utility.validate("out.txt", "Correct/jointest.txt", false);
    }
    public void hsplit(String rname, int joinkey, int index) throws Exception {
        // joinkey: the field index to hash; index: the index of outstream 0-3
        System.out.println("Hspliting " + rname + " on field [" + joinkey + "] with outsteam " + index);
        assert (index < GammaConstants.splitLen);
        ThreadList.init();
        Connector in = new Connector("input");
        ReadRelation r = new ReadRelation(rname, in);
        Connector[] out = new Connector[GammaConstants.splitLen];
        for (int i = 0; i < GammaConstants.splitLen; i++) {
            out[i]=new Connector("out" + i);
        }
        HSplit hs = new HSplit(in, joinkey, out);
        Print p = new Print(out[index]);
        ThreadList.run(p);
    }
    @Test
    public void hsplittest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        hsplit("parts", 0, 0);
        hsplit("parts", 0, 1);
        hsplit("parts", 0, 2);
        hsplit("parts", 0, 3);
        hsplit("odetails", 1, 0);
        hsplit("odetails", 1, 1);
        hsplit("odetails", 1, 2);
        hsplit("odetails", 1, 3);     
        RegTest.Utility.validate("out.txt", "Correct/hsplittest.txt", false);
    }
    public void merge(String rname, int joinkey) throws Exception {
        System.out.println("Merging " + rname + " on field [" + joinkey + "]");
        ThreadList.init();
        Connector in = new Connector("input");
        ReadRelation r = new ReadRelation(rname, in);
        Connector[] out = new Connector[GammaConstants.splitLen];
        for (int i = 0; i < GammaConstants.splitLen; i++) {
            out[i]=new Connector("out" + i);
        }
        HSplit hs = new HSplit(in, joinkey, out);
        Connector output = new Connector("output");
        Merge m = new Merge(out, output);
        Print p = new Print(output);
        ThreadList.run(p);
    }   
    @Test
    public void mergetest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        merge("parts", 0);
        merge("viewing",2);  
        RegTest.Utility.validate("out.txt", "Correct/mergetest.txt", false);
    }
    public void bmsimulator(String rname, int joinkey) throws Exception {
        System.out.println("BloomSimulator: " + rname + " on field [" + joinkey + "]");
        ThreadList.init();
        Connector outMap = new Connector("outMap");
        BloomSimulator bms = new BloomSimulator (rname, outMap, joinkey);
        PrintMap pm = new PrintMap(outMap);
        ThreadList.run(pm);
    }
    @Test
    public void bmsimulatortest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        bmsimulator("parts", 0);
        bmsimulator("odetails", 1);
        RegTest.Utility.validate("out.txt", "Correct/bmsimulatortest.txt", false);
    }
    public void bloom(String rname, int joinkey, int index) throws Exception {
        // joinkey: the field index to hash; index: the index of outstream 0-3
        System.out.println("Blooming " + rname + " on field [" + joinkey + "] with outsteam " + index);
        assert (index < GammaConstants.splitLen);
        ThreadList.init();
        Connector in = new Connector("input");
        ReadRelation r = new ReadRelation(rname, in);
        Connector[] out = new Connector[GammaConstants.splitLen];
        for (int i = 0; i < GammaConstants.splitLen; i++) {
            out[i] = new Connector("out" + i);
        }
        HSplit hs = new HSplit(in, joinkey, out);
        Connector outData = new Connector("outData");
        Connector outMap = new Connector("outMap");
        Bloom b = new Bloom(out[index], outData, outMap, joinkey);
        PrintMap pm = new PrintMap(outMap);
        ThreadList.run(pm);
    }
    @Test
    public void bloomtest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        bloom("parts", 0, 0);
        bloom("parts", 0, 1);
        bloom("parts", 0, 2);
        bloom("parts", 0, 3);   
        bloom("odetails", 1, 0);
        bloom("odetails", 1, 1);
        bloom("odetails", 1, 2);
        bloom("odetails", 1, 3);
        RegTest.Utility.validate("out.txt", "Correct/bloomtest.txt", false);
    }
   public void bfilter(String rname1, String rname2, int joinkey1, int joinkey2,int index) throws Exception {
        // joinkey: the field index to hash; index: the index of outstream 0-3
        System.out.println("Bfiltering " + rname1 + "on field [" + joinkey1 + "] with " + rname2 + "on field [" + joinkey2 + "] using outsteam " + index);
        assert (index < GammaConstants.splitLen);
        ThreadList.init();
        Connector input1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(rname1, input1);
        Connector input2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(rname2, input2);
        Connector[] out1 = new Connector[GammaConstants.splitLen];
        Connector[] out2 = new Connector[GammaConstants.splitLen];
        for (int i = 0; i < GammaConstants.splitLen; i++) {
            out1[i]=new Connector("out1" + i);
            out2[i]=new Connector("out2" + i);
        }
        HSplit hs1 = new HSplit(input1, joinkey1, out1);
        HSplit hs2 = new HSplit(input2, joinkey2, out2);
        Connector outData1 = new Connector("outData1");
        Connector outMap1 = new Connector("outMap1");
        Bloom b1 = new Bloom(out1[index], outData1, outMap1, joinkey1);
        Connector outData2 = new Connector("outData2");
        BFilter bf = new BFilter(out2[index], outMap1, outData2, joinkey2);
        Print p = new Print(outData2);
        ThreadList.run(p);
    }
        @Test
        public void bfiltertest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        bfilter("parts", "odetails", 0, 1, 0);
        bfilter("parts", "odetails", 0, 1, 1);
        bfilter("parts", "odetails", 0, 1, 2);
        bfilter("parts", "odetails", 0, 1, 3);
        RegTest.Utility.validate("out.txt", "Correct/bfiltertest.txt", false);
    }
        public void splitM(String rname, int joinkey, int index) throws Exception {
        System.out.println("SplitM: " + rname + " on field [" + joinkey + "] with stream " + index);
        assert (index < GammaConstants.splitLen);
        ThreadList.init();
        Connector outMap = new Connector("outMap");
        BloomSimulator bms = new BloomSimulator (rname, outMap, joinkey);
        Connector[] out = new Connector [GammaConstants.splitLen];
        for (int i = 0; i < GammaConstants.splitLen; i++) {
            out[i]=new Connector("out" + i);
        }
        SplitM sm = new SplitM (outMap, out);
        PrintMap pm = new PrintMap(out[index]);
        ThreadList.run(pm);
    }
        @Test
        public void splitMtest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        splitM("parts", 0, 0);
        splitM("parts", 0, 1);
        splitM("parts", 0, 2);
        splitM("parts", 0, 3);        
        RegTest.Utility.validate("out.txt", "Correct/splitMtest.txt", false);
    }    
        public void mergeM(String rname, int joinkey) throws Exception {
        System.out.println("MergeM: " + rname + " on field [" + joinkey + "]");
        ThreadList.init();
        Connector outMap = new Connector("outMap");
        BloomSimulator bms = new BloomSimulator (rname, outMap, joinkey);
        Connector[] out = new Connector[GammaConstants.splitLen];
        for (int i = 0; i < GammaConstants.splitLen; i++) {
            out[i]=new Connector("out" + i);
        }
        SplitM sm = new SplitM (outMap, out);
        Connector output = new Connector("output");
        MergeM mm = new MergeM (out, output);
        PrintMap pm = new PrintMap(output);
        ThreadList.run(pm);
    }
        @Test
        public void mergeMtest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        mergeM("parts", 0);       
        mergeM("odetails", 1);
        RegTest.Utility.validate("out.txt", "Correct/mergeMtest.txt", false);
    }
    
    
        public void hJoinRefineWithBloomFilters(String rname1, String rname2, int joinkey1, int joinkey2) throws Exception {
        System.out.println("HJoinRefineWithBloomFilterstest" + rname1 + " on field [" + joinkey1 + "] with " + rname2 + " on field ["+ joinkey2 + "]");
        ThreadList.init();
        HJoinRefineWithBloomFilters mrHJ = new HJoinRefineWithBloomFilters();
        Connector input1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(rname1, input1);
        Connector input2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(rname2, input2);
        Print p = new Print(mrHJ.join(input1,input2, joinkey1, joinkey2));
        ThreadList.run(p);
    }    
        @Test
        public void hJoinRefineWithBloomFilterstest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        hJoinRefineWithBloomFilters("parts", "odetails", 0, 1);
        hJoinRefineWithBloomFilters("client", "viewing", 0, 0);
        hJoinRefineWithBloomFilters("orders", "odetails", 0, 0);
        RegTest.Utility.validate("out.txt", "Correct/hJoinRefineWithBloomFilterstest.txt", false);
    }
   
        public void hJoinRefineWithParallelBloom_ParallelBF_Parallel_Join(String rname1, String rname2, int joinkey1, int joinkey2, int index) throws Exception {
        System.out.println("HJoinRefineWithParallelBloom_ParallelBF_Parallel_Join: " + rname1 + " on field [" + joinkey1 + "] with " + rname2 + " on field ["+ joinkey2 + "] stream: "+index);
        ThreadList.init();
        HJoinRefineWithParallelBloom_ParallelBF_Parallel_Join mrPHJ = new HJoinRefineWithParallelBloom_ParallelBF_Parallel_Join();
        Connector input1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(rname1, input1);
        Connector[] in1 = new Connector [GammaConstants.splitLen];
        Connector input2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(rname2, input2);
        Connector[] in2 = new Connector [GammaConstants.splitLen];
        for (int i=0; i<GammaConstants.splitLen; i++){
            in1[i] = new Connector("HSplit1-" + i);
            in2[i] = new Connector("HSplit2-" + i);
        }
        HSplit hs1 = new HSplit(input1,joinkey1,in1);
        HSplit hs2 = new HSplit(input2,joinkey2,in2);
        Print p = new Print(mrPHJ.join(in1,in2, joinkey1, joinkey2)[index]);
        ThreadList.run(p);
    }    
        @Test
        public void hJoinRefineWithParallelBloom_ParallelBF_Parallel_Jointest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        hJoinRefineWithParallelBloom_ParallelBF_Parallel_Join("parts", "odetails", 0, 1, 2);
        hJoinRefineWithParallelBloom_ParallelBF_Parallel_Join("client", "viewing", 0, 0, 3);
        hJoinRefineWithParallelBloom_ParallelBF_Parallel_Join("orders", "odetails", 0, 0, 0);
        RegTest.Utility.validate("out.txt", "Correct/hJoinRefineWithParallelBloom_ParallelBF_Parallel_Jointest.txt", false);
    }
        public void mapReduceHJoin(String rname1, String rname2, int joinkey1, int joinkey2) throws Exception {
        System.out.println("MapreducedHJoin" + rname1 + " on field [" + joinkey1 + "] with " + rname2 + " on field ["+ joinkey2 + "]");
        ThreadList.init();
        MapReduceHJoin mrBF = new MapReduceHJoin();
        Connector in1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(rname1, in1);
        Connector in2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(rname2, in2);
        Print p = new Print(mrBF.join(in1,in2, joinkey1, joinkey2));
        ThreadList.run(p);
    }  
        @Test
        public void mapReduceHJointest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        mapReduceHJoin("parts", "odetails", 0, 1);
        mapReduceHJoin("client", "viewing", 0, 0);
        mapReduceHJoin("orders", "odetails", 0, 0);
        RegTest.Utility.validate("out.txt", "Correct/mapreducedHJointest.txt", false);
   }
        public void gamma(String rname1, String rname2, int joinkey1, int joinkey2) throws Exception {
        System.out.println("GammaJoin: " + rname1 + " on field [" + joinkey1 + "] with " + rname2 + " on field ["+ joinkey2 + "]");
        ThreadList.init();
        Gamma g = new Gamma();
        Connector in1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(rname1, in1);
        Connector in2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(rname2, in2);
        Print p = new Print(g.join(in1,in2, joinkey1, joinkey2));
        ThreadList.run(p);
    }  
        @Test
        public void gammtest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        gamma("parts", "odetails", 0, 1);
        gamma("client", "viewing", 0, 0);
        gamma("orders", "odetails", 0, 0);
        RegTest.Utility.validate("out.txt", "Correct/gammatest.txt", false);
    }

        public void mapReduceBFilter(String rname1, String rname2, int joinkey1, int joinkey2) throws Exception {
        System.out.println("MapReduceBFilter: " + rname1 + " on field [" + joinkey1 + "] with " + rname2 + " on field ["+ joinkey2 + "]");
        ThreadList.init();
        MapReduceBFilter mrbl = new MapReduceBFilter();
        Connector in1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(rname1, in1);
        Connector in2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(rname2, in2);
        Connector bitMap = new Connector("bitMap of rname1");
        BloomSimulator bms = new BloomSimulator (rname1, bitMap, joinkey1);
        Print p = new Print(mrbl.join(in1,in2, bitMap, joinkey1, joinkey2));
        ThreadList.run(p);
    }
        @Test
        public void mapReduceBFiltertest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        mapReduceBFilter("parts", "odetails", 0, 1);
        mapReduceBFilter("client", "viewing", 0, 0);
        mapReduceBFilter("orders", "odetails", 0, 0);
        RegTest.Utility.validate("out.txt", "Correct/mapReduceBFiltertest.txt", false);
    }
        public void mapReduceBloom(String rname1, String rname2, int joinkey1, int joinkey2, String s) throws Exception {
        System.out.println("MapReduceBloom (" +s + " output): " + rname1 + " on field [" + joinkey1 + "] with " + rname2 + " on field ["+ joinkey2 + "]");
        ThreadList.init();
        MapReduceBloom mrbf = new MapReduceBloom();
        Connector in1 = new Connector("input1");
        ReadRelation r1 = new ReadRelation(rname1, in1);
        Connector in2 = new Connector("input2");
        ReadRelation r2 = new ReadRelation(rname2, in2);
        if (s.equals("data")){
            Print p = new Print(mrbf.join(in1,in2, joinkey1, joinkey2)[0]);
            ThreadList.run(p);
        }
        else{
            PrintMap pm = new PrintMap(mrbf.join(in1,in2, joinkey1, joinkey2)[1]);
            ThreadList.run(pm);
        }     
    } 
        @Test
        public void mapReduceBloomtest() throws Exception {
        RegTest.Utility.redirectStdOut("out.txt");
        mapReduceBloom("parts", "odetails", 0, 1,"data");
        mapReduceBloom("client", "viewing", 0, 0, "map");
        mapReduceBloom("orders", "odetails", 0, 0, "data");
        RegTest.Utility.validate("out.txt", "Correct/mapReduceBloomtest.txt", false);
    }  
}
