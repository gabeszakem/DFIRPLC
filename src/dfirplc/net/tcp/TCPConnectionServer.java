package dfirplc.net.tcp;

import dfirplc.MainApp;
import dfirplc.net.FillDataToBuffer;
import dfirplc.tools.ByteBufferToS7String;
import dfirplc.tools.ByteBufferToString;
import dfirplc.tools.ClassToString;
import dfirplc.tools.S7String;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import dfirplc.process.ProcessSelectDB;

/**
 *
 * @author gabesz
 */
public class TCPConnectionServer extends Thread {

    /**
     * A tcp kommunikációt megvalósitó osztály
     */
    @SuppressWarnings("FieldMayBeFinal")
    private TCPNet tcp;
    /**
     * A db package-ben található osztály ami megfeleltethető a PLC-ben db-ben
     * lévő struktúrával Itt tároljuk az utolsó elötti üzenetből kinyert
     * adatokat
     */
    public Object object;
    /**
     * A db package-ben található osztály ami megfeleltethető a PLC-ben db-ben
     * lévő struktúrával Itt tároljuk az utolsó üzenetből kinyert adatokat
     */
    public Object db;
    /**
     * Üzenet küldésre vagy fogadásra használjuk a kommunikációt
     */
    public int rw;
    /**
     * Buffer mérete
     */
    public int bufferSize;
    /**
     * Az üzeneteket tartalmazó "SOR"
     */
    public ArrayDeque<byte[]> bbs;
    /**
     * A rekordokat tartalmazó Lista
     */
    public ArrayList<Object> record;
    /**
     * DB mezőinek hosszának mérete
     */
    public int dBSize;

    /**
     * temp tömb a részüzenetek tárolására.
     */
    private byte[] temp;

    /**
     * Process kiválasztása.
     */
    @SuppressWarnings("FieldMayBeFinal")
    private ProcessSelectDB ProcessSelect;

    /**
     *
     * @param object DB88X class
     * @param plcPort A kommunikációhoz használt port száma
     * @param bufferSize Buffer mérete
     * @param ipAddress A PLC IP címe
     * @param rw Adat küldés vagy fogadás
     * @throws IOException
     */
    public TCPConnectionServer(Object object, int plcPort, int bufferSize, InetAddress ipAddress, int rw) throws IOException {
        tcp = new TCPNet(plcPort, ipAddress);  //Új TCP szerver inditása
        this.object = object;
        this.rw = rw;  //rw= false olvasunk, rw= true irunk az tcp-n keresztül
        this.bufferSize = bufferSize; //bufferSize inicializálása
        this.bbs = new ArrayDeque<>(); //bbs inicializálása
        this.record = new ArrayList(); //record inicializálása
        this.dBSize = ObjectSize.getSize(object); //dbsize kiszámolása
        try {
            //db inicializálása. (mezők létrehozása)
            this.db = this.object.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            /*
             * Hibaüzenetek kiírása
             */
            System.err.println(new Date() + " : " + ex.getMessage());
            MainApp.debug.printDebugMsg(db.getClass().getName(), TCPConnectionServer.class.getName(), "(error) TCPConnectionServer :", ex);
        }
    }

    @Override
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void run() {

        /*
         * TCP SERVER futtatása
         */
        if (rw == 0 || rw == 2) {
            while (true) {
                try {

                    /*
                     * Adatok fogadása TCP-n
                     */
                    byte[] receiveTelegram = tcp.receiveTelegram();
                    /*
                     * Telegramok beillesztése a "SOR"-ba
                     */
                    //bbs.add(receiveTelegram.clone());
                                            /*
                     * Idönként a telegramm nem egy rekordot ad vissza, hanem
                     * összegyűjt, több rekordot. Ezek a rekord nem bíztod hogy
                     * egész többszöröse a rekordnak ezért kezelni kell, hogy ne
                     * veszennek el az adatok.
                     */
                    if (receiveTelegram != null) {
                        if (receiveTelegram.length == this.dBSize) {
                            /*
                             * Ha a telegramm mérete megegyezzik, akkor nincs
                             * mit kezelni
                             */
                            if (temp != null) {
                                /*
                                 * Ha teljes telegramm jött, és a temp - ben
                                 * tárolunk rész rekordot akkor hiba van A hibát
                                 * kíirjuk.
                                 */
                                MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(error) TCPConnectionServer : Eldobjuk a temp tartalmát");
                                MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "\tbuffer tartalma: " + Arrays.toString(temp));
                                MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "\tbuffer mérete: " + temp.length);
                                temp = null;
                            }
                            /**
                             * Mezők feltöltése, és adatok feldolgozása.
                             */
                            field(receiveTelegram);
                        } else {
                            /**
                             * A telegtramm hossza nem megfelelő.
                             */
                            MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(warning) (" + this.db.getClass().getSimpleName() + ") TCPConnectionServer : Hibás telegramhossz: " + receiveTelegram.length);
                            /**
                             * temp tömbhöz hozzáfűzzük az új telegrammot.
                             */
                            temp = concat(temp, receiveTelegram);
                            /**
                             * Ha a temp tömbbe összegyűlt a teljes üzenet
                             */
                            if (temp.length % this.dBSize == 0) {
                                /**
                                 * Megszámoljuk az üzenetek számát
                                 */
                                int collectedRecord = temp.length / this.dBSize;
                                /**
                                 * minden üzenetet feldolgozzunk
                                 */
                                if (collectedRecord > 0) {
                                    for (int index = 0; index < collectedRecord; index++) {
                                        byte[] tmp = new byte[this.dBSize];
                                        System.arraycopy(temp, index * this.dBSize, tmp, 0, this.dBSize);
                                        field(tmp);
                                    }
                                    MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(" + this.db.getClass().getSimpleName() + ") TCPConnectionServer :Eltérő hosszúságú telegram feldolgozva");
                                }
                                /**
                                 * Kinullázzuk a tempet
                                 */
                                temp = null;
                            }
                        }
                    } else {
                        System.err.println(new Date() + " TCPConnectionServer: receiveTelegram = null");
                        MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(error) " + "(" + this.db.getClass().getSimpleName() + ") TCPConnectionServer : receiveTelegram = null");
                        MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(error) " + "(" + this.db.getClass().getSimpleName() + ") Numbers of element dequeu: " + bbs.size());
                    }

                    /*
                     * Debugolási célra a telegram sorszámának és a méretének
                     * kíirása
                     */
                    if (this.object.getClass().getName().equals("dfirplc.db.DB882") && MainApp.MESSAGELENGTHPRINTENABLE) {
                        String length;

                        if (receiveTelegram != null) {
                            length = Integer.toString(receiveTelegram.length);
                        } else {
                            length = "NULL";
                        }
                        MainApp.debug.printDebugMsg(db.getClass().getName(), TCPConnectionServer.class.getName(), ByteBuffer.wrap(receiveTelegram, 0, 2).getShort()
                                + ":: Telegramm hossza: " + length);
                    }
                } catch (Exception ex) {
                    /*
                     * Hiba esetén a hiba kiírása
                     */
                    System.err.println(new Date() + " " + this.getClass() + " " + ex.getMessage());
                    MainApp.debug.printDebugMsg(db.getClass().getName(), TCPConnectionServer.class.getName(), "(error) TCPConnectionServer :", ex);
                }
            }
        }
    }

    /**
     * Üzenetek küldése TCP-n keresztül
     */
    public void send() {
        if (MainApp.SENDTELEGRAMISENABLE) {//Ha az üzenetküldés engedélyezve van
            if (rw == 1 || rw == 2) {////Ha az üzenetküldésre használjuk a portot
                try {
                    /*
                     * Az üzenetek bájtokká alakítása, és átadása a TCPNET
                     * osztálynak küldésre
                     */
                    tcp.sendTelegram(FillDataToBuffer.load(object, bufferSize));
                } catch (IOException ex) {
                    /*
                     * Hiba esetén az üzenetek kíirása
                     */
                    System.err.println(ex.getMessage());
                    MainApp.debug.printDebugMsg(db.getClass().getName(), TCPConnectionServer.class.getName(), "(error) TCPConnectionServer :", ex);
                }
            }
        }
    }

    /**
     * Mezők feltöltése a db-ből kapott adatokkal
     */
    private synchronized void field(byte[] receiveTelegram) {
        /**
         * A db.-ben található mezők.
         */
        Field[] fields = this.db.getClass().getDeclaredFields();
        /**
         * A mutató kezdő címe.
         */
        int pointer = 0;
        /**
         * A "short" típusú adat hossza.
         */
        int shortLength = 2;
        /**
         * A "float" típusu adat hossza.
         */
        int floatLength = 4;
        try {
            /**
             * mezők bejárása, és értékek hozzárendelése
             */
            for (Field field : fields) {

                /**
                 * A mező adattípusának meghatározása
                 */
                String s = field.getType().getName();
                /**
                 * "short" típusú adat átalakítása, és hozzárendelése a mezőhöz.
                 */
                if (s.equals("short")) {
                    field.setShort(this.db, ByteBuffer.wrap(receiveTelegram, pointer, shortLength).getShort());
                    pointer += shortLength;
                    /**
                     * "float" típusú adat átalakítása, és hozzárendelése a
                     * mezőhöz.
                     */
                } else if (s.equals("float")) {
                    field.setFloat(this.db, ByteBuffer.wrap(receiveTelegram, pointer, floatLength).getFloat());
                    pointer += floatLength;
                    /**
                     * "java.lang.String" típusú adat átalakítása, és
                     * hozzárendelése a mezőhöz
                     */
                } else if (s.equals("java.lang.String")) {
                    field.set(this.db, ByteBufferToString.byteBufferToString(receiveTelegram, pointer));
                    /*
                     * A plc string adatnál elküldi a lefoglalt terület hosszát,
                     * és a string karakterlánc hosszát
                     */

                    /**
                     * pointer növelése a string hossza +2 -vel
                     */
                    pointer = (int) receiveTelegram[pointer] + pointer + 2;
                    /**
                     * "S7String" típusú adat átalakítása, és hozzárendelése a
                     * mezőhöz
                     */
                } else if (s.equals(S7String.class.getPackage().getName() + "." + "S7String")) {
                    /**
                     * S7String mező beállítása
                     */
                    field.set(this.db, ByteBufferToS7String.byteBufferToS7String(receiveTelegram, pointer));
                    /**
                     * Pointer nővelése Stringhossz + 2 vel
                     */
                    pointer = (int) receiveTelegram[pointer] + pointer + 2;
                } else {
                    /**
                     * Egyébb esetben nem feldolgozott adattípus.
                     */
                    System.out.println(new Date() + " " + this.getClass().getName() + " Nincs "
                            + s + " változó deklarálva.");
                    MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(warning) TCPConnectionServer : Nincs " + s + " változó deklarálva.");
                }

            }
            /**
             * Az adatok kiírása tesztelés céljából ha engedélyezett
             */
            if (MainApp.CLASSTOSTRINGENABLE) {
                if (!this.db.getClass().getName().equals("dfirplc.db.DB882")) {
                    if (!this.db.getClass().getName().equals("dfirplc.db.DB886")) {
                        System.out.println(ClassToString.toString(this.object));
                    }
                }
            }
            /**
             * Üzenetek feldolgozása
             */
            ProcessSelect.select(this);
        } catch (IllegalArgumentException | IllegalAccessException | IndexOutOfBoundsException ex) {
            /**
             * Hiba esetén üzenet írása a debug.log fájlba.
             */
            System.err.println(new Date() + " : " + ex);
            MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(error) TCPConnectionServer :", ex);
            try {
                MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "\tbuffer tartalma: " + Arrays.toString(receiveTelegram));
                MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "\tbuffer mérete: " + receiveTelegram.length);
            } catch (Exception e) {
                MainApp.debug.printDebugMsg(null, TCPConnectionServer.class.getName(), "(error) TCPConnectionServer :", e);
            }
        }
    }

    /**
     * Két bytetömb összefűzése
     */
    private byte[] concat(byte[] array1, byte[] array2) {
        if (array1 == null) {
            return array2;
        } else {
            byte[] mergedArray = new byte[array1.length + array2.length];
            System.arraycopy(array1, 0, mergedArray, 0, array1.length);
            System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
            return mergedArray;
        }
    }
}
