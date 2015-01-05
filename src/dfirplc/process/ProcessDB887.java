/*
 * Nem használjuk!!!!
 */
package dfirplc.process;

import dfirplc.MainApp;
import dfirplc.net.tcp.TCPConnectionServer;
import dfirplc.tools.CopyObjectValues;
import java.util.Date;
import dfirplc.db.DB887;

/**
 *
 * @author gkovacs02
 */
public class ProcessDB887 {
    /*
     * Az utolsó rekord
     */
    private static DB887 newDB887;
    /*
     * Az utolsó elötti record
     */
    private static DB887 dB887;
    

    /**
     * 
     * @param tcp
     */
    public static void select(TCPConnectionServer tcp) {
       
        
        try {
            /*
             * Az utolsó rekordot bemásoljuk az utolsó elötti rekordba
             */
            tcp.object = CopyObjectValues.copy(tcp.object, tcp.db);
        } catch (IllegalAccessException ex) {
            System.err.println(new Date() +" : " + ex.getMessage());
            MainApp.debug.printDebugMsg(null,ProcessDB887.class.getName(),"(error) SelectDB887 :",ex);
        }
    } 
}
