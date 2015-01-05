/*
 * A plc-ben üzenet küldéshez használatos DB alapján felépített
 * osztály Az adat típusok megfeleltetése: 
 * 
 * | PLC    | Java     | 
 * ----------------------
 * | int    | short    | 
 * | real   | float    |
 * | string | S7String | 
 */
package dfirplc.db;

/**
 *
 * @author Gabesz 
 *
 */
public class DB887 {
    /**
     * Távirat ID-t küldi a plc
     */
    public short LifeSignal = 0;
    
     public short LifeSignalError = 0;
}
