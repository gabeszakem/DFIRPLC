/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tools;

import java.lang.reflect.Field;

/**
 *
 * @author gkovacs02
 */
public class CopyObjectValues {

    /**
     * 
     * @param newObject
     * @param originalObject
     * @return Object
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object copy(Object newObject, Object originalObject ) throws IllegalArgumentException, IllegalAccessException {
        Field[] newFields = newObject.getClass().getDeclaredFields();
        Field[] originalFields = originalObject.getClass().getDeclaredFields();
        if (newFields.length == originalFields.length) {
            for (int index = 0; index < newFields.length; index++) {
                newFields[index].set(newObject, originalFields[index].get(originalObject));
            }
        }
         return newObject;
    }
}
