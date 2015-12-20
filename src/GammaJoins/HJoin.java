/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GammaJoins;

import basicConnector.*;
import gammaSupport.*;
import java.util.ArrayList;

/**
 *
 * @author so
 */
public class HJoin extends Thread implements GammaConstants{
    private ReadEnd input1;
    private ReadEnd input2;
    private int joinkey1;
    private int joinkey2;
    private WriteEnd out;
    
    private Relation r; 
    
    public HJoin (Connector c1, Connector c2, int jk1, int jk2, Connector o){
        this.input1 = c1.getReadEnd();
        this.input2 = c2.getReadEnd();
        this.joinkey1 = jk1;
        this.joinkey2 = jk2;
        this.out = o.getWriteEnd();
        // make a join relation r
        assert(c1.getRelation().getFieldNames()[jk1].equals(c2.getRelation().getFieldNames()[jk2]));
        r = Relation.join(c1.getRelation(), c2.getRelation(), jk1, jk2);
        out.setRelation(r);
        // ready to read data
        ThreadList.add(this);
    }
 
    @Override
    public void run() {
        ArrayList <Tuple> data = new ArrayList<Tuple>();
        try{
        //relation field
        Tuple t1 = input1.getNextTuple();
        Tuple t2 = input2.getNextTuple();
        out.putNextTuple(Tuple.join(t1, t2, joinkey1, joinkey2));
        //----------line
        t1 = input1.getNextTuple();
        t2 = input2.getNextTuple();
        out.putNextTuple(Tuple.join(t1, t2, joinkey1, joinkey2));
        //use arraylist<Tuple> data to temporally save all tuples in t1
        while ((t1 = input1.getNextTuple())!=null){
                data.add(t1);
             }
        while ((t2 = input2.getNextTuple())!=null){
               for (Tuple t : data){
                   if (t.get(joinkey1).equals(t2.get(joinkey2))){
                   out.putNextTuple( Tuple.join(t, t2, joinkey1, joinkey2) ); 
                   }
                }
        }
        out.close();
        } catch(Exception e) { ReportError.msg(this, e); }
    } 
}
