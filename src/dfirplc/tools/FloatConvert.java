/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tools;

/**
 *
 * @author gkovacs02
 */
public class FloatConvert {

    /**
     *
     * @param f
     * @return
     */
    public static float convertTo2Digits(float f) {
        return (float) ((float)(Math.round(f * 100)) / 100);
    }
}
