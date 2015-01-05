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

import dfirplc.tools.S7String;

/**
 *
 * @author Gabesz 
 *
 */
public class DB884 {

    
    /**
     * Anyag levéve az LCS-ről távirat ID-t küldi az OMRON
     */
    public short RemovePORTelId = 0;
    /**
     * Tekercsazonosító (tekercsszám)
     */
    public S7String CoilId = new S7String("", 14);
    /**
     * A vágás után képzett tekercsrész-szám [0..999]
     */
    public short CoilPartNo = 0;
}