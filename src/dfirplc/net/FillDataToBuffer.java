/*
 * Adatok áttalakítása buferré
 */
package dfirplc.net;

import static dfirplc.MainApp.debug;
import dfirplc.tools.S7String;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *
 * @author gabesz
 *
 */
public class FillDataToBuffer {

    /**
     *
     * @param db Ennek az osztálynak a mezőinek az értékeit teszi be a
     * bytebufferbe
     * @param buffeSize A lefoglalt buffer mérete
     * @return Az elkészített buferrel tér vissza.
     */
    @SuppressWarnings("UseSpecificCatch")
    public static byte[] load(Object db, int buffeSize) {
        /**
         * A DB.-ben található mezők
         */
        Field[] fields = db.getClass().getDeclaredFields();
        /**
         * A mutató kezdő címe.
         */
        int pointer = 0;
        /**
         * A "short" típusú adat hossza
         */
        int shortLength = 2;
        /**
         * A "float" típusú adat hossza
         */
        int floatLength = 4;
        /**
         * A "byte" típusu adat hossza
         */
        int byteLength = 1;
        /**
         * Üzenet telegram lefoglalása
         */
        byte[] sendTelegram = new byte[buffeSize];
        /**
         * A mezők bejárása
         */
        for (Field field : fields) {
            try {
                /**
                 * A mező adattípusának meghatározása
                 */
                String s = field.getType().getName();
                /**
                 * "short" típusu adat átalakítása, és hozzárendelése a
                 * bytebufferhez
                 */
                if (s.equals("short")) {
                    ByteBuffer.wrap(sendTelegram, pointer, shortLength).putShort(field.getShort(db));
                    pointer += shortLength;
                    /**
                     * "float" típusu adat átalakítása, és hozzárendelése a a
                     * bytebufferhez
                     */
                } else if (s.equals("float")) {
                    ByteBuffer.wrap(sendTelegram, pointer, floatLength).putFloat(field.getFloat(db));
                    pointer += floatLength;
                    /**
                     * "plctesztpc.tools.S7String" típusu adat átalakítása, és
                     * hozzárendelése a a bytebufferhez
                     */
                } else if (s.equals(S7String.class.getPackage().getName() + "." + "S7String")) {
                    S7String s7String = (S7String) field.get(db);
                    Byte size = (byte) s7String.getStringSize();
                    Byte length = (byte) s7String.length();
                    String string = s7String.getMyString();
                    /**
                     * A String lefoglalt mérete
                     */
                    ByteBuffer.wrap(sendTelegram, pointer, byteLength).put(size);
                    pointer += byteLength;
                    /**
                     * A String hossza
                     */
                    ByteBuffer.wrap(sendTelegram, pointer, byteLength).put(length);
                    pointer += byteLength;
                    /**
                     * A String
                     */
                    ByteBuffer.wrap(sendTelegram, pointer, size).put(stringToByteBuffer(string));
                    pointer += size;
                } else {
                    System.err.println(new Date() + " " + FillDataToBuffer.class.getName() + " Nincs "
                            + s + " változó deklarálva.");
                    debug.printDebugMsg(null,FillDataToBuffer.class.getName(),"(warning) FillDataToBuffer : Nincs " + s + " változó deklarálva.");
                }

            } catch (Exception ex) {
                System.err.println(ex);
                debug.printDebugMsg(null,FillDataToBuffer.class.getName(),"(error) ",ex);
            }
        }
        byte[] telegram = new byte[pointer];
        System.arraycopy(sendTelegram, 0, telegram, 0, pointer);
        return telegram;
    }

    private static byte[] stringToByteBuffer(String string) {
        return string.getBytes();
    }
}
