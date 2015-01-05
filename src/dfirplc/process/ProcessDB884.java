/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.process;

import dfirplc.MainApp;
import dfirplc.db.DB881;
import dfirplc.db.DB884;
import dfirplc.net.tcp.TCPConnectionServer;
import dfirplc.tools.CopyObjectValues;
import java.util.Date;

/**
 *
 * @author gkovacs02
 */
public class ProcessDB884 {

    private static DB884 newDB884;
    private static DB884 dB884;

    /**
     * 
     * @param tcp
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void select(TCPConnectionServer tcp) throws IllegalArgumentException, IllegalAccessException {
        dB884 = (DB884) tcp.object;
        newDB884 = (DB884) tcp.db;

        if (dB884.RemovePORTelId != newDB884.RemovePORTelId) {
            System.out.println(new Date().toString() + " - Lecsévélő tekercslevétel: " + newDB884.RemovePORTelId+": "+newDB884.CoilId.getMyString());
            MainApp.debug.printDebugMsg(null,ProcessDB884.class.getName(),"SelectDB884 : Lecsévélő tekercslevétel: " + newDB884.CoilId.getMyString() );
            for (int i = 0; i < MainApp.servers.length; i++) {
                 /**Nyugtázó telegramm küldéséhez a Lecsévélő tekercslevétel telegramm beállítása*/
                if (MainApp.servers[i].object.getClass().getName().equals("dfirplc.db.DB881")) {
                    DB881 db881=(DB881)MainApp.servers[i].object;
                    db881.RemovePORTelId=newDB884.RemovePORTelId;
                        MainApp.servers[i].send();            
                }
            }
        }
        try {
            tcp.object = CopyObjectValues.copy(tcp.object, tcp.db);
        } catch (IllegalAccessException ex) {
            System.err.println(new Date() +" : " + ex.getMessage());
            MainApp.debug.printDebugMsg(null,ProcessDB884.class.getName(),"(error) SelectDB884 :",ex);
        }
    }
}
