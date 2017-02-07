/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.sql;

import static dfirplc.MainApp.debug;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import dfirplc.tools.S7String;
import java.util.Date;

/**
 *
 * @author gabesz
 *
 */
public class RecordToBuffer {

    /**
     *
     * @param db Ennek az osztálynak a mezőinek az értékeit teszi be a
     * bytebufferbe
     * @param buffeSize A lefoglalt buffer mérete
     * @return Az elkészített buferrel tér vissza.
     */
    @SuppressWarnings("UseSpecificCatch")
    public static byte[] load(Object db, int buffeSize) {
        Field[] fields = db.getClass().getDeclaredFields(); // A DB.-ben található mezők
        int pointer = 0;    // A mutató kezdő címe
        int shortLength = 2; // A "short" típusu adat hossza
        int floatLength = 4; // A "float" típusu adat hossza
        int byteLength = 1; // A "byte" típusu adat hossza
        byte[] sendTelegram = new byte[buffeSize];

        for (Field field : fields) {
            try {
                // A mező adattípusának meghatározása
                String s = field.getType().getName();
                // "short" típusu adat átalakítása, és hozzárendelése a bytebufferhez
                if (s.equals("short")) {
                    ByteBuffer.wrap(sendTelegram, pointer, shortLength).putShort(field.getShort(db));
                    pointer += shortLength;
                    // "float" típusu adat átalakítása, és hozzárendelése a a bytebufferhez
                } else if (s.equals("float")) {
                    ByteBuffer.wrap(sendTelegram, pointer, floatLength).putFloat(field.getFloat(db));
                    pointer += floatLength;
                    // "plctesztpc.tools.S7String" típusu adat átalakítása, és hozzárendelése a a bytebufferhez
                } else if (s.equals(S7String.class.getPackage().getName() + "." + "S7String")) {
                    if (!field.getName().equals("CoilId")) {
                        S7String s7String = (S7String) field.get(db);
                        Byte size = (byte) s7String.getStringSize();
                        Byte length = (byte) s7String.length();
                        String string = s7String.getMyString();
                        ByteBuffer.wrap(sendTelegram, pointer, byteLength).put(size);//A String lefoglalt mérete
                        pointer += byteLength;
                        ByteBuffer.wrap(sendTelegram, pointer, byteLength).put(length);// A String hossza
                        pointer += byteLength;
                        ByteBuffer.wrap(sendTelegram, pointer, size).put(stringToByteBuffer(string)); // A String
                        pointer += size;
                    }
                } else {
                    System.err.println(new Date() + " " + RecordToBuffer.class.getName() + " Nincs "
                            + s + " változó deklarálva.");
                   debug.printDebugMsg(db.getClass().getSimpleName(),RecordToBuffer.class.getName(),"(warning) RecordToBuffer : Nincs "+s+" változó deklarálva.");
                }

            } catch (Exception ex) {
                System.err.println(ex);
               debug.printDebugMsg(db.getClass().getSimpleName(),RecordToBuffer.class.getName(),"(error) Eception:",ex);
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
