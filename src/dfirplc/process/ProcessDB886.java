/*
 *Szúrásterv letöltése a plc-be
 */
package dfirplc.process;

import dfirplc.MainApp;
import static dfirplc.MainApp.debug;
import dfirplc.db.DB885;
import dfirplc.db.DB886;
import dfirplc.net.tcp.TCPConnectionServer;
import tools.CopyObjectValues;
import java.util.Date;

/**
 *
 * @author gkovacs02
 */
public class ProcessDB886 {

    /**
     * Az utolsó rekord .
     */
    private static DB886 newDb886;
    /**
     * Állapotjelző bit, jelzi ha volt tekercs leküldés
     */
    private static boolean coilSent = false;
    /**
     * A szúrástervet adatok
     */
    private static DB885 db885;
    /**
     * A szúrásterv küldő szerver
     */
    private static TCPConnectionServer tcpDb885;

    /**
     *
     * @param tcp TCPConnectionServer
     */
    public static void select(TCPConnectionServer tcp) {
        /**
         * Ha engedélyezett a szúrásterv letöltése
         */
        if (MainApp.PASSSCHEDULEENABLE) {
            /**
             * Az utolsó elötti üzenet
             */
            newDb886 = (DB886) tcp.db;
            /**
             * A szúrásterv küldéshez tartozó TCPConnectionServer megkeresése
             */
            for (int i = 0; i < MainApp.servers.length; i++) {
                if (MainApp.servers[i].object.getClass().getName().equals("dfirplc.db.DB885")) {
                    db885 = (DB885) MainApp.servers[i].object;
                    tcpDb885 =MainApp.servers[i];
                }
            }

            if (coilSent) {
                /*
                 * Tekercsküldés volt
                 */
                if (db885.DwaPssSchTelId == newDb886.PassSchedTelId) {
                    /**
                     * Ha sikerült elküldeni a szúrástervet, akkor töröljük az
                     * adatbázisból, és a tekercs küldést jelző állapotbitet is
                     * töröljük
                     */
                    System.out.println(new Date().toString() + " - " + db885.CoilId.getMyString()
                            + " tekercs szúrásterve leadva. ");
                    debug.printDebugMsg(null, ProcessDB886.class.getName(), "SelectDB886 :" + db885.CoilId.getMyString()
                            + " tekercs szúrásterve leadva. ");
                    
                    MainApp.sql.updateSzurastervLog(db885);
                    MainApp.sql.deleteSzurasterv(db885);
                    coilSent = false;

                } else {
                    /*
                     * Nem sikerült elküldeni, ezért újraküldés
                     */
                    tcpDb885.send();
                }
            } else {
                /*
                 * Nem volt tekercs küldés
                 */


                if (newDb886.PassSchedRecHMI == 1) {
                    /*
                     * A plc készen áll az újabb szúrásterv küldésére
                     * ellenőrzés, hogy van -e új szúrásterv az adatbázisban
                     */
                    int count = MainApp.sql.countSzurasterv();
                    if (count > 0) {
                        /**
                         * Van újabb szúrásterv az adatbázisba, ezért letároljuk
                         */
                        DB885 db = MainApp.sql.readSzurasterv();
                        
                        if (db != null) {
                            
                            MainApp.sql.writeSzurastervLog(db);
                            /**
                             * Szúrásterv elküldése
                             */
                            tcpDb885.object = db;
                            tcpDb885.send();
                            /**
                             * Állapotjelző bit beállítása
                             */
                            coilSent = true;
                        } else {
                        }
                    }
                }
            }
        }


        try {

            tcp.object = CopyObjectValues.copy(tcp.object, tcp.db);
        } catch (IllegalAccessException ex) {
            System.err.println(new Date() + " : " + ex.getMessage());
            debug.printDebugMsg(null, ProcessDB886.class.getName(), "(error) SelectDB886 :", ex);
        }
    }
}
