/*
 * Nem használjuk!!!!
 */
package dfirplc.process;

import static dfirplc.MainApp.debug;
import dfirplc.net.tcp.TCPConnectionServer;
import tools.CopyObjectValues;
import java.util.Date;

/**
 *
 * @author gkovacs02
 */
public class ProcessDB881 {

    /**
     * 
     * @param tcp
     */
    public static void select(TCPConnectionServer tcp) {
        
        try {
            tcp.object = CopyObjectValues.copy(tcp.object, tcp.db);
        } catch (IllegalAccessException ex) {
            System.err.println(new Date() +" : " + ex.getMessage());
           debug.printDebugMsg(null,ProcessDB881.class.getName(),"(error) SelectDB881 :",ex);
        }
    }
    
}
