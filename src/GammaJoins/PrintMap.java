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
public class PrintMap extends Thread implements GammaConstants{
    private ReadEnd in;
    
    public PrintMap(Connector in) {
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
                for (int i=0; i<splitLen; i++){
                   for (int j=0; j<mapSize;j++){
                     System.out.print(input.charAt(i*mapSize+j)+" ");
                   }
                   System.out.println();
                }
            }
            System.out.flush();
        } catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }    
}
