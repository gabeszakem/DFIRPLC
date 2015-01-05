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
public class DB883 {

    
    /**
     * Anyag levéve az FCS-ről távirat ID-t küldi az OMRON
     */
    public short RemoveTRTelId = 0;
    /**
     * Tekercsazonosító (tekercsszám)
     */
    public S7String CoilId = new S7String("", 14);
    /**
     * A vágás után képzett tekercsrész-szám [0..999]
     */
    public short CoilPartNo = 0;
    /**
     * Lemez szélesség (mm)
     */
    public short CoilWidth = 0;
    /**
     * Tekercs súly mért
     */
    public short CoilMeasWeight = 0;
    /**
     * Lemez vastagság
     */
    public short CoilThickness = 0;
    /**
     * Tekercs súly számított
     */
    public short CoilCalWeight = 0;
}
