/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GammaJoins;

import basicConnector.*;
import gammaSupport.*;
import java.io.IOException;

/**
 *
 * @author so
 */
public class Sink extends Thread implements GammaConstants{
    private ReadEnd in;
    
    public Sink (Connector in) {
        this.in = in.getReadEnd();
	ThreadList.add(this);
	}
    public void run() {
        try {
            String input;
            while (true) {           
                input = in.getNextString();
                if ((input == null)) {
                    break;     
                }
            }
            System.out.flush();
        } catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }
    
}
