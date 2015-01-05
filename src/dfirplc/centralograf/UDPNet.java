/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.centralograf;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author gabesz
 */
public class UDPNet {

    DatagramSocket socket;
    byte[] receiveTelegram;
    InetAddress ipAddress;
    private int port;

    /**
     * 
     * @param port
     * @param buffersize
     * @param ipAddress
     * @throws IOException
     */
    public UDPNet(int port, int buffersize, InetAddress ipAddress) throws IOException {
        socket = new DatagramSocket(); // Az UDP számára Bind -olja a port-ot
        socket.setReuseAddress(true);     // Bind hiba elkerülése miatt
        receiveTelegram = new byte[buffersize]; //byte tömb a telegram fogadásához
        this.ipAddress = ipAddress;             // A partner IP Címe
        this.port = port;                       //port szám
    }

    /**
     * 
     * @return receiveTelegram
     * @throws IOException
     */
    public byte[] receiveTelegram() throws IOException {

        // Csomagok fogadása.
        DatagramPacket receivePacket = new DatagramPacket(receiveTelegram, receiveTelegram.length);
        // Csomagok fogadása socketben.
        socket.receive(receivePacket);
        try {
            /*
             * !!!! A "socket.receive(receivePacket)" -nek meg kell
             * előznie a "receivePacket.getAddress()" -t , mert különben null
             * pointer exception kivétel történik
             */

            // Ellenőrizzük hogy jó címről érkeztek az adtok
            if (receivePacket.getAddress().equals(ipAddress)) {
                // Adatok bemásolása a byte bufferbe
                receiveTelegram = receivePacket.getData();
            } else {
                //Nem jó helyről érkeztek az adatok
                receiveTelegram = null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return receiveTelegram;
    }

    /**
     * 
     * @param sendtelegram
     * @throws IOException
     */
    public void sendTelegram(byte[] sendtelegram) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(sendtelegram, sendtelegram.length, ipAddress, port);
        socket.send(sendPacket);
    }
}
