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
 */
public class DB885 {

    /**
     *Szurásterv azonosító
     */
    public short DwaPssSchTelId = 0;
    /**
     * DWA beállítási adat: tekercsazonosító	STRING[14]
     */
    public S7String CoilId = new S7String(new String(), 14);
    /**
     * DWA beállítási adat: Acélminőség	STRING[20]
     */
    public S7String DwaSteelGrade = new S7String(new String(), 20);
    /**
     * DWA beállítási adat: Szélesség [mm]
     */
    public float DwaWidth = 0.0f;
    /**
     * DWA beállítási adat: Vastagság [mm]
     */
    public float DwaThickness = 0.0f;
    /**
     * DWA beállítási adat: tekercssúly [kg]
     */
    public float DwaWeight = 0.0f;
    /**
     * DWA beállítási adat: tekercshossz [m]
     */
    public float DwaLength = 0.0f;
    /**
     * DWA beállítási adat: gyártandó tekercsek száma
     */
    public short DwaExitCoilsNo = 0;
    /**
     * DWA beállítási adat:az 1. Kész tekercs hossza [m]
     */
    public float DwaExitCoil1Length = 0.0f;
    /**
     * DWA beállítási adat: a 2. Kész tekercs hossza [m]
     */
    public float DwaExitCoil2Length = 0.0f;
    /**
     * DWA beállítási adat: folyamat típusa
     */
    public short DwaProcessType = 0;
    /**
     * DWA beállítási adat: nyúlás [%]
     */
    public float DwaElongation = 0.0f;
    /**
     * DWA beállítási adat: hengerlési erő [kN]
     */
    public float DwaRollForce = 0.0f;
    /**
     * DWA beállítási adat: hajlító erő [kN]
     */
    public float DwaBendingForce = 0.0f;
    /**
     * DWA beállítási adat: sori sebesség [m/min]
     */
    public float DwaLineSpeed = 0.0f;
    /**
     * DWA beállítási adat: alap felszórt mennyiség [l/min]
     */
    public float DwaBasicSprayAmount = 0.0f;
    /**
     * DWA beállítási adat: FEszítés Beo S ggő-állvány [N/mm2]
     */
    public float DwaTensionPorEsBr = 0.0f;
    /**
     * DWA beállítási adat: FEszítés Beo S ggő-állvány [N/mm2]
     */
    public float DwaTensionEsBrStd = 0.0f;
    /**
     * DWA beállítási adat: Feszítés Állv-Kio Sggő [N/mm2]
     */
    public float DwaTensionStdXsBr = 0.0f;
    /**
     * DWA beállítási adat: Feszítés KioSggős-FCS [N/mm2]
     */
    public float DwaTensionXsBrT = 0.0f;
    /**
     * DWA beállítási adat: magátmérő (belső menetek) [mm]
     */
    public float DWACoreDiameter = 0.0f;
    /**
     * DWA beállítási adat: nyúlás alsó határ
     */
    public float DwaElongLowLim = 0.0f;
    /**
     * DWA beállítási adat: Nyúlás felső határ
     */
    public float DwaElongUpLim = 0.0f;
}