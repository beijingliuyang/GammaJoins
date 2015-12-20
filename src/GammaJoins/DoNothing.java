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
public class DoNothing extends Thread implements GammaConstants {
    private ReadEnd in;
    private WriteEnd out;
    
    public DoNothing(Connector in, Connector out) {
		this.in = in.getReadEnd();
		this.out = out.getWriteEnd();
                out.setRelation(in.getRelation());
		ThreadList.add(this);
	}
    
    @Override
    public void run() {
        try {
            String input = in.getNextString();
            while (input!=null) {  
                out.putNextString(input);
                input = in.getNextString();
            }
            out.close();
        } catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }	
}
