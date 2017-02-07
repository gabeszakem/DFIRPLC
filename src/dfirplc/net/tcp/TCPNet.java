package dfirplc.net.tcp;

import dfirplc.MainApp;
import static dfirplc.MainApp.debug;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.Date;

/**
 *
 * @author gabesz
 */
public class TCPNet {

    private ServerSocket serverSocket;
    private byte[] receiveTelegram;
    private InetAddress ipAddress;
    private int port;
    private Socket clientSocket = null;

    /**
     * @param port port száma
     * @param ipAddress PLC ip címe
     * @throws IOException TCP Socket létrehozása
     */
    public TCPNet(int port, InetAddress ipAddress) throws IOException {
        serverSocket = new ServerSocket(port); //A Socket létrehozása
        /*
         * Üzenet kiírása
         */
        System.out.println(new Date().toString() + " TCP szerver létrehozása a " + port + " porton...");
        debug.printDebugMsg(null, TCPNet.class.getName(), " TCP szerver létrehozása a " + port + " porton...");
        serverSocket.setReuseAddress(true);     // Bind hiba elkerülése miatt
        /*
         * Üzenet vesztés miatt lett beírva, de vélhetően nem ez okozta a
         * problémát
         */
        serverSocket.setReceiveBufferSize(65536);
        this.port = port;                          //port szám
        this.ipAddress = ipAddress;              //IP Address
        /*
         * Üzenet kiírása
         */
        System.out.println(new Date().toString() + " TCP szerver figyel a " + port + " porton...");
        debug.printDebugMsg(null, TCPNet.class.getName(), " TCP szerver figyel a " + port + " porton...");
    }

    /**
     * @return receiveTelegram A fogadott telegrammal tér vissza byte[]
     * @throws IOException Az üzeneteket fogadja
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public synchronized byte[] receiveTelegram() throws IOException {
        boolean result = false;
        /*
         * Ha nincs socket a plc-vel illetve a socket zárva van akkor
         * létrehozunk egy új kliens socket-et
         */
        if (clientSocket == null || clientSocket.isClosed()) {
            if (clientSocket != null) {
                /*
                 * Hiba esetén Üzenet kiírása
                 */
                System.out.println(new Date().toString() + " Kliens (" + clientSocket.getRemoteSocketAddress() + ") lekapcsolódott a " + this.port + " porton.");
                debug.printDebugMsg(null, TCPNet.class.getName(), " Kliens (" + clientSocket.getRemoteSocketAddress() + ") lekapcsolódott a " + this.port + " porton.");
            }
            //Szerver várakozik a kliens kapcsolatra
            this.clientSocket = serverSocket.accept();
            /*
             * Üzenet vesztés miatt lett beírva, de vélhetően nem ez okozta a
             * problémát
             */
            this.clientSocket.setReceiveBufferSize(65536);
            /*
             * Üzenet kiírása
             */
            System.out.println(new Date().toString() + " Kliens (" + clientSocket.getRemoteSocketAddress() + ") kapcsolódott a " + this.port + " porton.");
            debug.printDebugMsg(null, TCPNet.class.getName(), " Kliens (" + clientSocket.getRemoteSocketAddress() + ") kapcsolódott a " + this.port + " porton.");
        }
        try {
            /**
             * !!!! A "serverSocket.receive(receivePacket)" -nek meg kell
             * előznie a "receivePacket.getAddress()" -t , mert különben null
             * pointer exception kivétel történik
             */
            /*
             * Ellenőrizzük hogy jó címről érkeztek az adatok
             */
            if (clientSocket.getInetAddress().equals(ipAddress)) {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "ISO-8859-1"));
                CharBuffer cbuf = CharBuffer.allocate(65536);
                int bufsize = inFromClient.read(cbuf);

                String retString = "";
                if (bufsize == -1) {
                    clientSocket.close();
                    result = false;
                    debug.printDebugMsg(null, TCPNet.class.getName(), " TCPNet bufsize = -1 a " + clientSocket.getRemoteSocketAddress() + "kilens lekapcsolódott ");
                } else {
                    for (int i = 0; i < bufsize; i++) {
                        retString = retString + cbuf.get(i);
                    }
                    // Adatok bemásolása a byte bufferbe
                    receiveTelegram = retString.getBytes("ISO-8859-1");
                    result = true;
                }
            } else {
                /*
                 * Nem ami plc-nk kapcsolódott
                 *
                 */
                System.out.println(new Date().toString() + " Kliens (" + clientSocket.getRemoteSocketAddress() + ") kapcsolat bezárása");
                debug.printDebugMsg(null, TCPNet.class.getName(), " Kliens (" + clientSocket.getRemoteSocketAddress() + ") kapcsolat bezárása");
                /*
                 * Az üzenetet eldobjuk
                 */
                result = false;
                /*
                 * A kapcsolatot bezárjuk
                 */
                clientSocket.close();
            }
        } catch (Exception ex) {
            /*
             * Hiba esetén Üzenet kiírása
             */
            System.err.println(new Date().toString() + " " + this.getClass() + " " + ex.getMessage());
            debug.printDebugMsg(null, TCPNet.class.getName(), "(error) TCPNet :", ex);
            /*
             * A kapcsolatot bezárjuk
             */
            clientSocket.close();
        }
        if (result) {
            return receiveTelegram;
        } else {
            return null;
        }
    }

    /**
     *
     * @param sendtelegram Az elküldésre váró üzenet (byte[])
     * @throws IOException
     */
    public void sendTelegram(byte[] sendtelegram) throws IOException {
        /*
         * Ha nincs socket a plc-vel illetve a socket zárva van akkor
         * létrehozunk egy új kliens socket-et
         */
        if (clientSocket == null || clientSocket.isClosed()) {
            if (clientSocket != null) {
                /*
                 * Hiba esetén Üzenet kiírása
                 */
                System.out.println(new Date().toString() + " Kliens (" + clientSocket.getRemoteSocketAddress() + ") lekapcsolódott a " + this.port + " porton.");
                debug.printDebugMsg(null, TCPNet.class.getName(), " Kliens (" + clientSocket.getRemoteSocketAddress() + ") lekapcsolódott a " + this.port + " porton.");
            }
            //Szerver várakozik a kliens kapcsolatra
            this.clientSocket = serverSocket.accept();
            /*
             * Üzenet kiírása
             */
            System.out.println(new Date().toString() + " Kliens (" + clientSocket.getRemoteSocketAddress() + ") kapcsolódott a " + this.port + " porton.");
            debug.printDebugMsg(null, TCPNet.class.getName(), " Kliens (" + clientSocket.getRemoteSocketAddress() + ") kapcsolódott a " + this.port + " porton.");
        }
        try {
            /*
             * Új DataOutputStream létrehozása
             */
            DataOutputStream outToClient = new DataOutputStream(this.clientSocket.getOutputStream());
            /*
             * Üzenet String-gé alakítása
             */
            String str = new String(sendtelegram, "ISO-8859-1");
            /*
             * Üzenet küldése
             */
            outToClient.writeBytes(str);
        } catch (Exception ex) {
            /*
             * Hiba esetén Üzenet kiírása
             */
            System.err.println(new Date().toString() + " " + this.getClass() + " " + ex.getMessage());
            debug.printDebugMsg(null, TCPNet.class.getName(), "(error) TCPNet :", ex);
            /*
             * A kapcsolatot bezárjuk
             */
            this.clientSocket.close();
        }
    }
}
