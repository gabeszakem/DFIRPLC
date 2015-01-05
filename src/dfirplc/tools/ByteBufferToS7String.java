/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tools;

/**
 *
 * @author gkovacs02
 */
public class ByteBufferToS7String {

    /**
     * 
     * @param array
     * @param offset
     * @return S7String
     */
    public static S7String byteBufferToS7String(byte[] array, int offset) {
        String string = ByteBufferToString.byteBufferToString(array, offset);
        Integer size = (int) array[offset];
        return new S7String(string, size);
       
    }
}
