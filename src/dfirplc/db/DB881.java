/*
 * A plc-ben üzenet küldéshez használatos DB alapján felépített
 * osztály Az adat típusok megfeleltetése: 
 * 
 * | PLC    | Java     | 
 * ----------------------
 * | int    | short    | 
 * | real   | float    |
 * | string | S7String | 
 *
 * DB881 ben a DB883 illetve a DB884 fogadott üzenetek id -ját küldjük
 * vissza a siemens plc-nek
 */
package dfirplc.db;

/**
 *
 * @author Gabesz
 */
public class DB881 {

    /**
     * Anyag levéve az FCS-ről távirat ID-t küldi az OMRON
     */
    public short RemoveTRTelId = 0;
    /**
     * Anyag levéve az LCS-ről távirat ID-t küldi az OMRON
     */
    public short RemovePORTelId = 0;
}
