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
public class DB886 {
    /**
     * Távirat ID-t küldi az OMRON
     */
    public short PassSchedTelId = 0;
    /**
     * Pass Schedule Received by HMI (1- ready for next telegram)
     */
    public short PassSchedRecHMI = 0;
}
