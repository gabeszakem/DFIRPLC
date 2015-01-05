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
public class DB882 {

    /**
     * távirat azonosító szám [1..999]
     */
    public short TelegrId = 0;
    /**
     * Tekercsazonosító (tekercsszám)
     */
    public S7String CoilId = new S7String("", 14);
    /**
     * A vágás után képzett tekercsrész-szám [0..999]
     */
    public short CoilPartNo = 0;
    /**
     * A feldolgozás típusa [0-száraz, 1-nedves]
     */
    public short ProcType = 0;
    /**
     * Nyúlás alapjel (a szúrástervből)
     */
    public float ElongRef = 0.0f;
    /**
     * Tényleges nyúlás (a TCS -től)
     */
    public float ElongAct = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 1 zóna
     */
    public float FlatDevZone1 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 2 zóna
     */
    public float FlatDevZone2 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 3 zóna
     */
    public float FlatDevZone3 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 4 zóna
     */
    public float FlatDevZone4 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 5 zóna
     */
    public float FlatDevZone5 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 6 zóna
     */
    public float FlatDevZone6 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 7 zóna
     */
    public float FlatDevZone7 = 0.0f;
    /**
     *  // Síkfekvés-hiba (a TCS-től) 8 zóna
     */
    public float FlatDevZone8 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 9 zóna
     */
    public float FlatDevZone9 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 10 zóna
     */
    public float FlatDevZone10 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 11 zóna
     */
    public float FlatDevZone11 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 12 zóna
     */
    public float FlatDevZone12 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 13 zóna
     */
    public float FlatDevZone13 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 14 zóna
     */
    public float FlatDevZone14 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 15 zóna
     */
    public float FlatDevZone15 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 16 zóna
     */
    public float FlatDevZone16 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 17 zóna
     */
    public float FlatDevZone17 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 18 zóna
     */
    public float FlatDevZone18 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 19 zóna
     */
    public float FlatDevZone19 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 20 zóna
     */
    public float FlatDevZone20 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 21 zóna
     */
    public float FlatDevZone21 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 22 zóna
     */
    public float FlatDevZone22 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 23 zóna
     */
    public float FlatDevZone23 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 24 zóna
     */
    public float FlatDevZone24 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 25 zóna
     */
    public float FlatDevZone25 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 26 zóna
     */
    public float FlatDevZone26 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 27 zóna
     */
    public float FlatDevZone27 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 28 zóna
     */
    public float FlatDevZone28 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 29 zóna
     */
    public float FlatDevZone29 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 30 zóna
     */
    public float FlatDevZone30 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 31 zóna
     */
    public float FlatDevZone31 = 0.0f;
    /**
     * Síkfekvés-hiba (a TCS-től) 32 zóna
     */
    public float FlatDevZone32 = 0.0f;
    /**
     * Hengerlési sebesség [m/min] (a TCS-től)
     */
    public float RollingSpeed = 0.0f;
    /**
     * Hengerlési erő összesen CSO + HO [kN]
     */
    public float HGCRollForceAct = 0.0f;
    /**
     * Hengerlési erő eltérés HO - CSO [kN]
     */
    public float HGCRollForceDiffAct = 0.0f;
    /**
     * Hengerhajlítás [kN]
     */
    public float WRBending = 0.0f;
    /**
     * Feszítés a lecsévélő és a bemenő S-ggő között [kN]
     */
    public float TensionPOR_EsBr;
    /**
     * Feszítés a bemenő S-ggő és a hengerállvány között [kN]
     */
    public float TensionEsBr_Ms = 0.0f;
    /**
     * Feszítés hengerállvány és a kimenő S-ggő között [kN]
     */
    public float TensionMs_XsBr = 0.0f;
    /**
     * Feszítés kimenő S-ggő és a felcsévélő között [kN]
     */
    public float TensionXsBr_TR = 0.0f;
    /**
     * Kihengerelt hossz a felcsévélőn [m]
     */
    public float TrRollStripLength = 0.0f;
}
