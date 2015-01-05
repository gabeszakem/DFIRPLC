/**
 * A hengerléshez kapcsolódo 100 ms -ként küldött rekordokat dolgozzuk fel
 */
package dfirplc.process;

import dfirplc.MainApp;
import dfirplc.db.DB882;
import dfirplc.net.tcp.TCPConnectionServer;
import dfirplc.sql.RecordToBuffer;
import dfirplc.tools.CloneObject;
import dfirplc.tools.CopyObjectValues;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Gabesz
 */
public class ProcessDB882 {
    /*
     * Az utolsó rekord
     */
    private static DB882 newDB882;
    /*
     * Az utolsó elötti record
     */
    private static DB882 dB882;
    /*
     * A hengerlés kezdetének az időpontja
     */
    private static String start_time;
    /*
     * A hengerlés befejezésének az időpontja
     */
    private static String stop_time;

    /**
     *
     * @param tcp
     */
    public static void select(TCPConnectionServer tcp) {
        /*
         * Dátum megjelenítésének a formátuma
         */
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*
         * Az utolsó elötti üzenet
         */
        dB882 = (DB882) tcp.object;
        /*
         * Az utolsó üzenet
         */
        newDB882 = (DB882) tcp.db;
        /*
         * Amikor a program elindul akkor beállítjuk a tekercs kezdő idejének az
         * aktuális időpontot
         */
        if (start_time == null) {
            start_time = sdf.format(new Date());
            System.out.println("program start:" + start_time);
        }
        /*
         * Ha a tekercset nem szedték le a csévélőről, és a tekercs száma
         * megegyezik az előző rekord tekercs számával, akkor úgyanaz a tekercs
         * van bent az állványban.
         */
        if (dB882.CoilId.getMyString().equals(newDB882.CoilId.getMyString()) && !MainApp.CoilRemoveFromTensionReal) {
            stop_time = sdf.format(new Date());
            /*
             * A tekercsnek van száma (Eltérő eset pl. program újrainditás)
             */
            if (!dB882.CoilId.getMyString().equals("")) {
                try {
                    /*
                     * A rekordot, hozzáadjuk az új rekordhoz.
                     */
                    tcp.record.add(CloneObject.clone(tcp.db));
                } catch (InstantiationException | IllegalAccessException ex) {
                    System.err.println(new Date() + " : " + ex.getMessage());
                    MainApp.debug.printDebugMsg(null, ProcessDB882.class.getName(), "(error) SelectDB882 :", ex);
                }
            }
        } else {/*
             * Új tekercset hengerelnek, vagy leszedték a régi tekercset.
             */
            /*
             * A tekercslevételt jelző bitet false-ba álítjuk
             */
            MainApp.CoilRemoveFromTensionReal = false;
            /*
             * Lekérdezzük 1db rekord méretét byte-ban
             */
            int recordSize = tcp.record.size();
            /*
             * Meghatározzuk a rekord méretét a tekercs száma
             * nélkül(Feleslegesnek tartottam a rekordba beletenni a tekercs
             * számát is, mert az mindig megegyezik. Ha a tekercs száma
             * megváltozna, akkor új rekord készülne. )
             */
            int dBSize = tcp.dBSize - (newDB882.CoilId.getStringSize()) - 2;
            /*
             * csak akkor végezzük el az alábbi műveleteket, ha vannak rekordok
             */
            if (recordSize > 0) {
                System.out.println(new Date().toString() + " - " + dB882.CoilId.getMyString()
                        + " tekercs elkészült. "
                        + tcp.record.size() + " record rögzítve");
                System.out.println(new Date().toString() + " - " + "Tekercs súly a mérlegtől: "+MainApp.removedCoilData.CoilMeasWeight);
                System.out.println(new Date().toString() + " - " + "Tekercs súly (számított): "+MainApp.removedCoilData.CoilCalWeight);
                MainApp.debug.printDebugMsg(null, ProcessDB882.class.getName(), "SelectDB882 :" + dB882.CoilId.getMyString()
                        + " tekercs elkészült. "
                        + tcp.record.size() + " record rögzítve");
                /*
                 * Új bájt tömb létrehozása (rekordok száma * 1 rekord mérete)
                 * mérettel
                 */
                byte[] buffer = new byte[dBSize * recordSize];
                /*
                 * rekordok átalakítása byte tömbbé
                 */
                for (int index = 0; index < recordSize; index++) {
                    byte[] tmpBuff = RecordToBuffer.load(tcp.record.get(index), tcp.bufferSize);
                    System.arraycopy(tmpBuff, 0, buffer, index * dBSize, dBSize);
                }
                /*
                 * A rekodokat tároló tömbnél felszabadítjuk a memóriát
                 */
                tcp.record = new ArrayList();
                System.out.println(new Date().toString() + " - " + dB882.CoilId.getMyString()
                        + " bytebuffer mérete: "
                        + buffer.length);
                MainApp.debug.printDebugMsg(null, ProcessDB882.class.getName(), "SelectDB882 :" + dB882.CoilId.getMyString()
                        + " bytebuffer mérete: "
                        + buffer.length);
                /*
                 * A végeredményt adatbázisba mentjük.
                 */
                MainApp.sql.record(start_time, stop_time, dB882.CoilId.getMyString(), buffer);
                
            }
            start_time = sdf.format(new Date());
        }
        try {
            /*
             * Az utolsó rekordot bemásoljuk az utolsó elötti rekordba
             */
            tcp.object = CopyObjectValues.copy(tcp.object, tcp.db);
        } catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
            MainApp.debug.printDebugMsg(null, ProcessDB882.class.getName(), "(error) SelectDB882 :", ex);
        }
    }
}
