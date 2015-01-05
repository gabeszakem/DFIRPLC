/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tools;

import dfirplc.MainApp;
import java.lang.reflect.Field;
import java.util.Date;

/**
 *
 * @author gabesz
 */
public class ClassToString {

    /**
     * 
     * @param object
     * @return String
     */
    public static String toString(Object object) {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        if (MainApp.CLASSTOSTRINGENABLE) {
            result.append(object.getClass().getName());
            result.append(" Object {");
            result.append(newLine);

            //determine fields declared in this class only (no fields of superclass)
            Field[] fields = object.getClass().getDeclaredFields();

            //print field names paired with their values
            for (Field field : fields) {
                result.append("  ");
                try {
                    result.append(field.getName());
                    result.append(": ");
                    //requires access to private field:
                    if (field.getType().getName().equals(S7String.class.getPackage().getName() + "." + "S7String")) {
                        S7String s7String = (S7String) field.get(object);
                        result.append(s7String.getMyString());
                    } else {
                        result.append(field.get(object));
                    }
                } catch (IllegalAccessException ex) {
                    System.err.println(new Date() +" : " + ex.getMessage());
                    MainApp.debug.printDebugMsg(null,ClassToString.class.getName(),"(error) ClassTOString :",ex);
                }
                result.append(newLine);
            }
            result.append("}");
        }
        return result.toString();
    }
}
