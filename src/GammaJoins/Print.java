/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GammaJoins;

import basicConnector.*;
import gammaSupport.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author so
 */
public class Print extends Thread implements GammaConstants{
    private ReadEnd in;
    private List <String> output = new ArrayList<String>();
    
    public Print(Connector c) {
        this.in = c.getReadEnd();
        ThreadList.add(this);
    }
    
    @Override
    public void run() {
        try {
            String input;
            while (true) {           
                input = in.getNextString();
                if ((input == null)) {
                    break;     
                } 
                output.add(input.substring(2));
 //               System.out.println(input.substring(2));
            }
            printing (output);
            System.out.flush();
        } catch (Exception e) {
            ReportError.msg(this.getClass().getName() + e);
        }
    }
    public void printing (List<String> content){
        // recover table space information based on ------ line
        String temp = content.get(1);
        StringTokenizer st = new StringTokenizer(temp,"#");
        int size = st.countTokens(); 
        // save the number of "-" in each field in an int array
        int i = 0;
        int[] num = new int [size];  
        while (st.hasMoreElements()) {
           String s = st.nextToken();
           num[i++]=s.length();
        }
        //printout each tuple with the correct space added based on information saved in num[]
        for (i=0; i<content.size();i++)
        {
              st = new StringTokenizer(content.get(i),"#");
              size = st.countTokens();
              int k=0;
              while (st.hasMoreElements()) {
                String s = st.nextToken();
                System.out.print(s);
                int diff = num[k++]-s.length();
                for (int j=0; j<diff; j++){
                    System.out.print(" ");
                }
                System.out.print(" ");
           }
           System.out.println();
        } 
    }   
}
