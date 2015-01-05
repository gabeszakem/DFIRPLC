/*
 * A kommunikációhoz szükséges struktúra
 */
package dfirplc.net;

/**
 *
 * @author Gabesz
 */
public class CommStruct {

    /**
     * Adatbázis
     */
    public Object db;
    /**
     * port
     */
    public int port;
    /**
     * olvasás vagy irás
     */
    public int rw;

    /**
     *
     * @param DB DB a plc szerint.
     * @param port port száma.
     * @param rw Írás vagy olvasás
     */
    public CommStruct(Object DB, int port, int rw) {
        this.db = DB;
        this.port = port;
        this.rw = rw;
    }
}
