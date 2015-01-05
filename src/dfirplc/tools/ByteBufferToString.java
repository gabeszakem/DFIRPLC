/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tools;

import java.util.Arrays;

/**
 *
 * @author gkovacs02
 */
public class ByteBufferToString {
    /**
     * 
     * @param array
     * @param offset
     * @return String
     */
    public static String byteBufferToString(byte[] array, int offset) {
        /**
         * byte[] @array ami a PLC telegrammból nyerünk ki. Egy 14 karakterből
         * álló String :
         *
         *
         * - 0. byte a Sztring álltal lefoglalt terület hossza - 1. byte a
         * hasznos karakterek száma a stringben - Majd az első bájtban
         * meghatározott hosszúságú karakter lánc
         */
        Integer length = (int) array[offset];
        Integer begin = offset + 2;
        Integer end = offset + 2 + length;
        byte[] b=Arrays.copyOfRange(array, begin, end);
        int newEnd=0;
        for(int i=0; i<b.length;i++){
            if(b[i]==0){
                newEnd=i;
            }
        }

        return new String(Arrays.copyOfRange(b, 0, newEnd));

    }
}
