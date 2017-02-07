package dfirplc;

/**
 *
 * @author gabesz
 */
import dfirplc.centralograf.CentalografMessage;
import dfirplc.centralograf.UDPConnectionServer;
import dfirplc.db.*;
import dfirplc.form.DressFrame;
import dfirplc.form.Frame_;
import form.TextAreaLogProgram;
import dfirplc.net.CommStruct;
import dfirplc.net.RW;
import dfirplc.net.tcp.TCPConnectionServer;
import dfirplc.sql.SQL;
import tools.Debug;
import dfirplc.tray.Tray;
import form.LogViewer;
import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import table.ColorStruct;

/**
 *
 * @author Gabesz
 */
public class MainApp {

    /**
     * Buffer méret. (BUFFERSIZE = 1024)
     */
    private static final Integer BUFFERSIZE = 1024;
    /**
     * PLC IP címe. (IPADDRESS = "192.168.210.11")
     */
    private static final String IPADDRESS = "192.168.210.11";
    /**
     * Frame engedélyezése vagy tiltása. (FRAMEISENABLED = true)
     */
    public static final boolean FRAMEISENABLED = true;

    /**
     * DressPanel engedélyezése vagy tiltása. (DRESSPANELISENABLED = true)
     */
    public static final boolean DRESSPANELISENABLED = true;
    /**
     * Loggolás textareában engedélyezése vagy tiltása. (LOGPANELISENABLED =
     * true)
     */
    public static final boolean LOGPANELISENABLED = true;
    /**
     * Telegram küldése engedélyezett vagy sem. (SENDTELEGRAMISENABLE = true)
     */
    public static final boolean SENDTELEGRAMISENABLE = true;
    /**
     * Az osztályok printelésének engedélyezése. (CLASSTOSTRINGENABLE = false)
     */
    public static final boolean CLASSTOSTRINGENABLE = false;
    /**
     * A fogadott üzenetek hosszának kíírásának engedélyezése.
     * (MESSAGELENGTHPRINTENABLE = false)
     */
    public static final boolean MESSAGELENGTHPRINTENABLE = false;

    /**
     * Ha engedélyezzük , akkor udp kapcsolaton keresztül küldi a berendezés
     * állapotát a centralográf terminálnak. (CENTRALOGGRAFMESSAGEENABLE = true)
     */
    public static final boolean CENTRALOGGRAFMESSAGEENABLE = true;
    /**
     * A Centrál terminál ip címe. (CENTRALOGGRAFIPADDRESS = "10.1.39.154")
     */
    public static final String CENTRALOGGRAFIPADDRESS = "10.1.39.154";
    /**
     * TCP Szerverek tömbje.
     */
    public static TCPConnectionServer[] servers;
    /**
     * SQL utasítások.
     */
    public static SQL sql;
    /**
     * debug.txt fájlba kiírja az üzeneteket. LOGMOD =0 Új log minden program
     * újraindításakor LOGMODE =1 Naptári nap körbejárásával tárolás.
     */
    public static Debug debug;
    /**
     * Álapotjelző bit : felcsévélőröl levették a tekercset.
     */
    public static boolean CoilRemoveFromTensionReal = false;
    /**
     * Szúrásterv leküldés engedélyezése. (PASSSCHEDULEENABLE = true)
     */
    public static final boolean PASSSCHEDULEENABLE = true;
    /**
     * Difirtől jövő adatok táblázatban.
     */
    public static Frame_ frame;
    /**
     * A berendezés aktuális állapota technológiai képernyőn.
     */
    public static DressFrame dress;
    /**
     * A berendezés loggolásának kijelzése a képernyőn.
     */
    public static TextAreaLogProgram textAreaLog;
    /**
     * UDP kapcsolat a centralograf terminállal.
     */
    public static UDPConnectionServer udpCentralograf;
    /**
     * Centralográfnak küldött üzenet.
     */
    public static CentalografMessage centralografMessage = new CentalografMessage();
    /**
     * Az utoljára levett tekercs.
     */
    public static DB883 removedCoilData = new DB883();
    /**
     * Logviewer engedélyezése
     */
    public static boolean LOGVIEWERISENABLED=true;
    
    public static LogViewer logViewer;

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        debug = new Debug(true, 1,"DFIRPLC");

        debug.printDebugMsg(null, MainApp.class.getName(), "Program elidítva...");
        if (LOGPANELISENABLED) {
            debug.printDebugMsg(null, MainApp.class.getName(), "Logpanel engedélyezve...");
            try {
                /**
                 * Új logPanel létrehozása.
                 */
                textAreaLog = new TextAreaLogProgram(debug);

            } catch (Exception ex) {
                System.out.println("LogPanel inditása nem sikerült");
                debug.printDebugMsg(null, MainApp.class.getName(), "(error) Logpanel indítása nem sikerült...", ex);
            }
        } else {
            debug.printDebugMsg(null, MainApp.class.getName(), "Logpanel letiltva...");
        }
        System.out.println("PLC IP címe: " + IPADDRESS);
        debug.printDebugMsg(null, MainApp.class.getName(), "PLC IP címe: " + IPADDRESS);
        /**
         * PLC IP Címe InetAddress formátumban.
         */
        InetAddress IPAddress = InetAddress.getByName(IPADDRESS);
        /**
         * Inicializáslás @commStructs A kommunikációhoz szükséges beállitások
         * tömbje
         */
        final CommStruct[] commStructs = new CommStruct[]{
            new CommStruct(new DB881(), 2010, RW.WRITE),
            new CommStruct(new DB882(), 2011, RW.READ),
            new CommStruct(new DB883(), 2012, RW.READ),
            new CommStruct(new DB884(), 2013, RW.READ),
            new CommStruct(new DB885(), 2014, RW.WRITE),
            new CommStruct(new DB886(), 2015, RW.READ),
            new CommStruct(new DB887(), 2016, RW.RW)
        };

        /**
         * @db A DB... osztályok tömbje @ports A kommunikációhoz használt portok
         * száma. @rw rw olvass vagy ír a buszon
         */
        final Object[] db = new Object[commStructs.length];
        /**
         * portok számának halmaza.
         */
        int[] ports = new int[commStructs.length];
        /**
         * Írás vagy olvasás.
         */
        final int[] rw = new int[commStructs.length];
        /**
         * Adatok feltőltése.
         */
        for (int index = 0; index < commStructs.length; index++) {
            db[index] = commStructs[index].db;
            ports[index] = commStructs[index].port;
            rw[index] = commStructs[index].rw;
        }
        /**
         *
         * Új thread indítása a PC-n a TCP csomagok fogadására a
         * "hengerlesiAdatokPort" porton. Az adatok a "hengerlesiAdatokDB" -be
         * fognak bekerülni. Az IPADDRESS azért szükséges, hogy csak attól
         * fogadjuk a portra érkező üzeneteket, akitől várjuk.
         *
         */
        servers = new TCPConnectionServer[db.length];
        /**
         * TCP szerverek létrehozása, elindítása
         */
        for (int index = 0; index < db.length; index++) {
            TCPConnectionServer tcp = new TCPConnectionServer(db[index],
                    ports[index], BUFFERSIZE, IPAddress, rw[index]);
            servers[index] = tcp;
            servers[index].start();
            servers[index].setName("TCPConnecionServer(" + db[index].getClass().getName() + ")");
        }
        /**
         * Ha a frame engedélyezve van akkor elindítjuk.
         *
         */
        if (FRAMEISENABLED) {
            /**
             * Új Frame létrehozása.
             */
            debug.printDebugMsg(null, MainApp.class.getName(), "Frame engedélyezve...");
            try {
                frame = new Frame_();
                /**
                 * Panelek létrehozása.
                 */
                frame.createPanels(servers);
                /**
                 * Időzítő létrehozása.
                 */
                Thread timer = new Thread("Timer(Frame refresh)") {

                    @Override
                    public void run() {
                        while (true) {
                            try {
                                synchronized (this) {
                                    wait(500);
                                    /**
                                     * A frame frisítése, időzitve.
                                     */
                                    frame.refreshPanels(servers);
                                }
                            } catch (Exception ex) {
                                debug.printDebugMsg(null, MainApp.class.getName(), "Frame thread hiba...", ex);
                            }
                        }
                    }
                };
                timer.start();

                /**
                 * Frame láthatóvá tétele.
                 */
                //frame.setVisible(true);
                System.out.println("Frame engedélyezve...");
                debug.printDebugMsg(null, MainApp.class.getName(), "Frame elindítva...");

            } catch (Exception ex) {
                System.out.println("Frame inditása nem sikerült");
                debug.printDebugMsg(null, MainApp.class.getName(), "Frame indítása nem sikerült...");
            }
        } else {
            debug.printDebugMsg(null, MainApp.class.getName(), "Frame letíltva...");
        }

        if (DRESSPANELISENABLED) {
            debug.printDebugMsg(null, MainApp.class.getName(), "Dresspanel engedélyezve...");
            try {
                /**
                 * Új Frame létrehozása.
                 */
                dress = new DressFrame(servers);
                /**
                 * Időzítő létrehozása.
                 */
                Thread timer2 = new Thread("Timer(DressPanel refresh)") {

                    @Override
                    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
                    public void run() {
                        while (true) {
                            try {
                                synchronized (this) {
                                    wait(1000);
                                    /**
                                     * A frame frisítése, időzitve.
                                     */
                                    dress.refreshPanels();
                                }
                            } catch (Exception ex) {
                                debug.printDebugMsg(null, MainApp.class.getName(), "Dresspanel timer hiba...", ex);
                            }
                        }
                    }
                };
                timer2.start();
                System.out.println("DressPanel engedélyezve...");
                debug.printDebugMsg(null, MainApp.class.getName(), "Dresspanel elindítva...");
            } catch (Exception ex) {
                System.out.println("DressPanel inditása nem sikerült");
                debug.printDebugMsg(null, MainApp.class.getName(), "Dresspanel inditása nem sikerült...");
            }
        } else {
            debug.printDebugMsg(null, MainApp.class.getName(), "Dresspanel letíltva...");
        }
        
        if (LOGVIEWERISENABLED) {
            logViewer = new LogViewer(debug);
            ArrayList<ColorStruct> colors = new ArrayList();
            colors.add(new ColorStruct(1, new Color(243, 149, 149), "error"));
            colors.add(new ColorStruct(1, new Color(233, 173, 32), "warning"));
            //colors.add(new ColorStruct(2, Color.GREEN, "(Húzvaegyengető)"));
            //colors.add(new ColorStruct(2, Color.yellow, "(Georg hasító)"));
            //colors.add(new ColorStruct(2, Color.ORANGE, "(1550-es hasító)"));
            colors.add(new ColorStruct(1, new Color(204, 229, 255), "info"));
            logViewer.setColor(colors);
        }

        /**
         * SQL ADATBázis kapcsolat létrehozása.
         */
        sql = new SQL();
        System.out.println("SQL kapcsolat létrehozva");
        debug.printDebugMsg(null, MainApp.class.getName(), "SQL kapcsolat létrehozva...");
        

        /**
         * tray (tálca) elindítása...
         */
        try {
            debug.printDebugMsg(null, MainApp.class.getName(), "Tray inditása...");
            Tray tray = new Tray();
            debug.printDebugMsg(null, MainApp.class.getName(), "Tray elindítva");
        } catch (Exception ex) {
            System.out.println("Tray inditása nem sikerült");
            debug.printDebugMsg(null, MainApp.class.getName(), "Tray inditása nem sikerült...");
        }
        
        
        
        
        
        if (CENTRALOGGRAFMESSAGEENABLE) {
            debug.printDebugMsg(null, MainApp.class.getName(), "Centralográf üzenet engedélyezve...");
            int centralografPort = 2100;
            int bufferSize = 2048;
            InetAddress centralografIPAddress = InetAddress.getByName(CENTRALOGGRAFIPADDRESS);
            udpCentralograf = new UDPConnectionServer(centralografMessage, centralografPort, bufferSize, centralografIPAddress);
            udpCentralograf.createPanels(servers);
            udpCentralograf.start();
            debug.printDebugMsg(null, MainApp.class.getName(), "Centralográf üzenet létrehozva UDP-n : " + CENTRALOGGRAFIPADDRESS
                    + ":" + centralografPort);

        } else {
            debug.printDebugMsg(null, MainApp.class.getName(), "Centralográf üzenet letiltva...");
        }
        
        
        
    }
}
