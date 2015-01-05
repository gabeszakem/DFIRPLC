/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tools;

/**
 *
 * @author Operator
 */
public class S7String {

    @SuppressWarnings("FieldMayBeFinal")
    private int stringSize;
    private String myString;

    /**
     * 
     * @param myString
     * @param stringSize
     */
    public S7String(String myString, int stringSize) {
        this.stringSize = stringSize;
        if (myString.length() > this.stringSize) {
            this.myString = myString.substring(0,this.stringSize-1);
        } else {
            this.myString = myString;
        }
    }

    /**
     * 
     * @return A Sztring lefoglalt területének a méretével tér vissza
     */
    public int getStringSize() {
        return stringSize;
    }

    /**
     * 
     * @return A Sztring hosszával tér vissza.
     */
    public int length() {
        return myString.length();
    }

    /**
     * 
     * @return A Sztringgel tér vissza.
     * 
     */
    public String getMyString() {
        return myString.trim();
    }

    /**
     * 
     * @param string
     */
    public void setMyString(String string) {
        if (string.length() > stringSize) {
            this.myString = string.substring(0, stringSize - 1).trim();
        } else {
            this.myString = string.trim();
        }
    }
}
