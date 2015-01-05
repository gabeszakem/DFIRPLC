/*
 * Az object méretének a meghatározása
 */
package dfirplc.net.tcp;

import dfirplc.MainApp;
import dfirplc.tools.S7String;
import java.lang.reflect.Field;
import java.util.Date;

/**
 *
 * @author gabesz
 */
public class ObjectSize {

    /**
     *
     * @param object /"db class"
     * @return A "db class" mezőinek a hosszával tér vissza
     */
    @SuppressWarnings("UseSpecificCatch")
    public static int getSize(Object object) {
        /**
         * A DB.-ben található mezők
         */
        Field[] fields = object.getClass().getDeclaredFields();
        /**
         * A mutató kezdő címe
         */
        int pointer = 0;
        /**
         * A "short" típusu adat hossza
         */
        int shortLength = 2;
        /**
         * A "float" típusu adat hossza
         */
        int floatLength = 4;

        for (Field field : fields) {
            try {
                /**
                 * A mező adattípusának meghatározása
                 */
                String s = field.getType().getName();
                /**
                 * "short" típusu adat átalakítása, és hozzárendelése a bytebufferhez
                 */
                if (s.equals("short")) {
                    pointer += shortLength;
                    /**
                     * "float" típusu adat átalakítása, és hozzárendelése a a bytebufferhez
                     */
                } else if (s.equals("float")) {
                    pointer += floatLength;
                    /**
                     * "plctesztpc.tools.S7String" típusu adat átalakítása, és
                     * hozzárendelése a a bytebufferhez
                     */
                } else if (s.equals(S7String.class.getPackage().getName() + "." + "S7String")) {
                    S7String s7String = (S7String) field.get(object);
                    Byte size = (byte) s7String.getStringSize();
                    pointer += size;
                    pointer += 2;
                } else {
                    System.err.println(new Date() + " " + ObjectSize.class.getName() + " Nincs "
                            + s + " változó deklarálva.");
                    MainApp.debug.printDebugMsg(null,ObjectSize.class.getName(),"(warning) ObjectSize : Nincs " + s + " változó deklarálva.");
                }

            } catch (Exception ex) {
                System.err.println(ex);
                MainApp.debug.printDebugMsg(null,ObjectSize.class.getName(),"(error) Exception:",ex);
            }
        }
        return pointer;
    }
}
