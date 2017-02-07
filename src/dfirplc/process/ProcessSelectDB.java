/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.process;

import dfirplc.MainApp;
import static dfirplc.MainApp.debug;
import dfirplc.net.tcp.TCPConnectionServer;
import java.util.Date;

/**
 *
 * @author gkovacs02
 */
public class ProcessSelectDB {

    /**
     * Processec elindítására szolgáló osztály
     */
    public ProcessSelectDB() {
    }
   
    /**
     * 
     * @param tcp
     * Eldöntjük, hogy a TCPConnectionServer melyik DB-t használja. A használt
     *DB szerinti ProcessSelectDB lesz használva.
     */
    public void select(TCPConnectionServer tcp) {
        String dbName = tcp.object.getClass().getName();
        if (dbName.contains("DB881")) {
            ProcessDB881.select(tcp);
        } else if (dbName.contains("DB882")) {
            ProcessDB882.select(tcp);
        } else if (dbName.contains("DB883")) {
            ProcessDB883.select(tcp);
        } else if (dbName.contains("DB884")) {
            try {
                ProcessDB884.select(tcp);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                System.err.println(new Date() + ex.getMessage());
               debug.printDebugMsg(null,ProcessSelectDB.class.getName(),"(error) SelectDB :",ex);
            }
        } else if (dbName.contains("DB885")) {
            ProcessDB885.select(tcp);
        } else if (dbName.contains("DB886")) {
            ProcessDB886.select(tcp);
        }  else if (dbName.contains("DB887")) {
            ProcessDB887.select(tcp);
        }else {
            System.err.println(new Date() + " : " + this.getClass().getName() + " : Nincs ilyen DB");
           debug.printDebugMsg(null,ProcessSelectDB.class.getName(),"(warning) SelectDB : Nincs ilyen DB");
        }
    }
}
