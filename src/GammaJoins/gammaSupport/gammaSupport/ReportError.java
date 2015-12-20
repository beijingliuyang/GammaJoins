/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gammaSupport;

/**
 *
 * @author dsb
 */
public class ReportError extends java.lang.Error {

    public ReportError( String msg ) { super(msg); }

    public static void msg( Thread t, Exception e ) {
        throw new ReportError( t.getClass().getName() + " error: " + e.getMessage());
    }

    public static void msg( String m ) {
        throw new ReportError( "error: " + m);
    }
}
