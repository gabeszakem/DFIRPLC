/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.process;

import dfirplc.MainApp;
import dfirplc.db.DB881;
import dfirplc.db.DB883;
import dfirplc.net.tcp.TCPConnectionServer;
import dfirplc.tools.CopyObjectValues;
import java.util.Date;

/**
 *
 * @author gkovacs02
 */
public class ProcessDB883 {

    private static DB883 newDB883;
    private static DB883 dB883;

    /**
     *
     * @param tcp
     */
    public static void select(TCPConnectionServer tcp) {
        dB883 = (DB883) tcp.object;
        newDB883 = (DB883) tcp.db;
        if (dB883.RemoveTRTelId != newDB883.RemoveTRTelId) {
            /** A tekercslevétel bit státuszát beállítjuk */
            MainApp.CoilRemoveFromTensionReal = true;
            try {
                MainApp.removedCoilData = (DB883) CopyObjectValues.copy(MainApp.removedCoilData, newDB883);
            } catch (IllegalAccessException ex) {
                System.err.println(new Date() + " : " + ex.getMessage());
                MainApp.debug.printDebugMsg(null, ProcessDB883.class.getName(), "(error) SelectDB883 :", ex);
            }
            System.out.println(new Date().toString() + " - Felcsévélő tekercslevétel: " + newDB883.CoilId.getMyString());
            MainApp.debug.printDebugMsg(null, ProcessDB883.class.getName(), "SelectDB883 : Felcsévélő tekercslevétel: " + newDB883.CoilId.getMyString());
            for (int i = 0; i < MainApp.servers.length; i++) {
                if (MainApp.servers[i].object.getClass().getName().equals("dfirplc.db.DB881")) {
                    /**
                     * Nyugtázó telegramm küldéséhez a felcsévélő tekercslevétel
                     * telegramm beállítása
                     */
                    DB881 db881 = (DB881) MainApp.servers[i].object;
                    db881.RemoveTRTelId = newDB883.RemoveTRTelId;
                    MainApp.servers[i].send();
                } else if (MainApp.servers[i].object.getClass().getName().equals("dfirplc.db.DB882")) {
                    ProcessDB882.select(MainApp.servers[i]);
                }
            }

        }
        try {
            /**
             * db Beállítása
             */
            tcp.object = CopyObjectValues.copy(tcp.object, tcp.db);
        } catch (IllegalAccessException ex) {
            System.err.println(new Date() + " : " + ex.getMessage());
            MainApp.debug.printDebugMsg(null, ProcessDB883.class.getName(), "(error) SelectDB883 :", ex);
        }
    }
}
