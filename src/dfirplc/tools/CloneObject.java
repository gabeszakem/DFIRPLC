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
public class CloneObject {

    /**
     * 
     * @param originalObject
     * @return Object
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object clone(Object originalObject) throws InstantiationException, IllegalAccessException {
        Object newObject = originalObject.getClass().newInstance();
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
