/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.centralograf;

import dfirplc.MainApp;
import dfirplc.db.DB882;
import dfirplc.db.DB887;
import dfirplc.net.tcp.TCPConnectionServer;
import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author gabesz
 */
public class UDPConnectionServer extends Thread {

    @SuppressWarnings("FieldMayBeFinal")
    private UDPNet udp;
    @SuppressWarnings("FieldMayBeFinal")
    private Object object;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean rw;
    @SuppressWarnings("FieldMayBeFinal")
    private int bufferSize;
    private DB882 db882;
    private DB887 db887;
    private short previousTelegramId = 0;
    private short previousLifeSignal = 0;
    private static long timestamp = System.currentTimeMillis();
    private static boolean run = false;
    private static int count = 0;
    private static final int MAXCOUNT = 30;

    /**
     *
     * @param Object
     * @param plcPort
     * @param bufferSize
     * @param ipAddress
     * @throws IOException
     */
    public UDPConnectionServer(Object Object, int plcPort, int bufferSize, InetAddress ipAddress) throws IOException {
        udp = new UDPNet(plcPort, bufferSize, ipAddress);  //Új UDP szerver inditása
        this.object = Object;
        this.bufferSize = bufferSize;

    }

    /**
     *
     * @param servers
     */
    @SuppressWarnings("ConvertToStringSwitch")
    public void createPanels(TCPConnectionServer[] servers) {
        /**
         * Megkeressük a DB882 -t és a DB887 -et
         */
        for (TCPConnectionServer server : servers) {
            try {
                if (server.object.getClass().getSimpleName().equals("DB882")) {
                    this.db882 = (DB882) server.object;
                } else if (server.object.getClass().getSimpleName().equals("DB887")) {
                    this.db887 = (DB887) server.object;
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                MainApp.debug.printDebugMsg(null, UDPConnectionServer.class.getName(), "(error) createPanel :", ex);
            }
        }
    }

    @Override
    public void run() {

        Thread timer = new Thread("Timer(Centralograf message)") {

            @Override
            @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
            public void run() {
                while (true) {
                    try {
                        synchronized (this) {
                            wait(900);
                            centralograf();
                            if (run) {
                                MainApp.centralografMessage.plantStatus = 1;
                            } else {
                                MainApp.centralografMessage.plantStatus = 0;
                            }
                            try {
                                if (previousLifeSignal != db887.LifeSignal) {
                                    if (count < 0) {
                                        if (count < MAXCOUNT) {
                                            MainApp.debug.printDebugMsg(null, UDPConnectionServer.class.getName(), "(error) Thread(\"Timer(Centralograf message)\") :", "previousLifeSignal != db887.LifeSignal"
                                                    + " A PLC-től nem érkezett életjel. UDP kapcsolat blokkolva. count: " + count
                                                    + " LifeSignal: " + db887.LifeSignal + "LifeSignalError: " + db887.LifeSignalError);
                                        } else {
                                            MainApp.debug.printDebugMsg(null, UDPConnectionServer.class.getName(), "(warning) Thread(\"Timer(Centralograf message)\") :", "previousLifeSignal != db887.LifeSignal"
                                                    + " A PLC-től nem érkezett életjel. UDP kapcsolat blokkolva. count: " + count
                                                    + " LifeSignal: " + db887.LifeSignal + "LifeSignalError: " + db887.LifeSignalError);
                                        }
                                    }
                                    udp.sendTelegram(FillDataToBuffer.load(object, bufferSize));
                                    count = 0;
                                } else {
                                    if (count == 0) {
                                        MainApp.debug.printDebugMsg(null, UDPConnectionServer.class.getName(), "(warning) Thread(\"Timer(Centralograf message)\") :", "previousLifeSignal != db887.LifeSignal"
                                                + " A PLC-től nem érkezett életjel.  LifeSignal: " + db887.LifeSignal + "LifeSignalError: " + db887.LifeSignalError);
                                    }
                                    if (count < MAXCOUNT) {
                                        udp.sendTelegram(FillDataToBuffer.load(object, bufferSize));
                                    }
                                    count++;
                                }

                            } catch (Exception e) {
                                e.printStackTrace(System.err);
                                MainApp.debug.printDebugMsg(null, UDPConnectionServer.class.getName(), "(error) Thread(\"Timer(Centralograf message)\") :", e);

                            } finally {
                                previousLifeSignal = db887.LifeSignal;
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(System.err);
                        MainApp.debug.printDebugMsg(null, UDPConnectionServer.class.getName(), "(error) Thread(\"Timer(Centralograf message)\") :", ex);
                    }
                }
            }
        };
        timer.start();
    }

    private void centralograf() {
        if (db882.TelegrId != previousTelegramId) {
            timestamp = System.currentTimeMillis();
        }
        if ((System.currentTimeMillis() - timestamp) < 1000) {
            run = true;
        } else {
            run = false;
        }
        previousTelegramId = db882.TelegrId;
    }
}
