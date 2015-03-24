/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.sql;

/**
 *
 * @author gabesz
 */
import dfirplc.MainApp;
import dfirplc.db.DB885;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author gkovacs02
 */
public class SQL {

    @SuppressWarnings("FieldMayBeFinal")
    private String url;
    @SuppressWarnings("FieldMayBeFinal")
    private String user;
    @SuppressWarnings("FieldMayBeFinal")
    private String password;
    private static int pasSchID;

    /**
     * SQL kapcsolatot deklaráló konstruktor.
     */
    public SQL() {
        this.url = "jdbc:mysql://localhost:3306/dfir";
        this.user = "gabriel";
        this.password = "Qwer1234";
    }

    /**
     *
     * @param start_time
     * @param stop_time
     * @param htek_id
     * @param record
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void record(String start_time, String stop_time, String htek_id, byte[] record) {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            String query = "INSERT INTO regf(htek_id, start_time, stop_time, record, status, coil_part_number,"
                    + " coil_width, coil_meas_weight, coil_cal_weight, coil_thickness) VALUES (?,?,?,?,?,?,?,?,?,?)";
            st = con.prepareStatement(query);
            st.setString(1, htek_id.trim());
            st.setString(2, start_time);
            st.setString(3, stop_time);
            st.setBlob(4, new ByteArrayInputStream(record), record.length);
            st.setInt(5, 0);
            st.setInt(6, MainApp.removedCoilData.CoilPartNo);
            st.setInt(7, MainApp.removedCoilData.CoilWidth);
            st.setInt(8, MainApp.removedCoilData.CoilMeasWeight);
            st.setInt(9, MainApp.removedCoilData.CoilCalWeight);
            st.setInt(10, MainApp.removedCoilData.CoilThickness);
            st.executeUpdate();
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new java.util.Date());
            String message = "INSERT INTO regf"
                    + "(htek_id, start_time, stop_time, record, status, coil_part_number,"
                    + " coil_width, coil_meas_weight, coil_cal_weight, coil_thickness)"
                    + "VALUES"
                    + "('" + htek_id + "','" + start_time + "','" + stop_time + "','record, 0, "
                    + MainApp.removedCoilData.CoilPartNo + "','"
                    + MainApp.removedCoilData.CoilWidth + "','"
                    + MainApp.removedCoilData.CoilMeasWeight + "','"
                    + MainApp.removedCoilData.CoilCalWeight + "','"
                    + MainApp.removedCoilData.CoilThickness + "')";
            System.out.println(new java.util.Date().toString() + " - " + message);
            MainApp.debug.printDebugMsg(null, SQL.class.getName(), message);
            int count = 0;
            try {
                query = "UPDATE semaphore SET table_status='0' , time_stamp='" + date
                        + "' WHERE table_name='regf'";
                System.out.println(new java.util.Date().toString() + " - " + query);
                MainApp.debug.printDebugMsg(null, SQL.class.getName(), query);
                st = con.prepareStatement(query);
                count = st.executeUpdate();
            } catch (Exception ex) {
                System.out.println(new java.util.Date().toString() + " - " + ex.getMessage());
                MainApp.debug.printDebugMsg(null, SQL.class.getName(), "Update semaphor error", ex);
            }
            if (count == 0) {
                query = "INSERT INTO semaphore (table_name, table_status, time_stamp) VALUES (?,?,?)";
                st = con.prepareStatement(query);
                st.setString(1, "regf");
                st.setString(2, "0");
                st.setString(3, date);

                String queryMess = "INSERT INTO semaphore (table_name, table_status, time_stamp) VALUES ('regf','0','" + date + "')";
                System.out.println(new java.util.Date().toString() + " - " + queryMess);
                MainApp.debug.printDebugMsg(null, SQL.class.getName(), queryMess);

                st.executeUpdate();
            }

            // System.out.println(new java.util.Date().toString() + " - " + query);
            // MainApp.debug.printDebugMsg(null, SQL.class.getName(), query);
        } catch (SQLException ex) {
            MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
            ex.printStackTrace(System.err);

        } finally {

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

        }
    }

    /**
     *
     * @return count.
     */
    public int countSzurasterv() {
        Connection con = null;
        PreparedStatement st = null;
        @SuppressWarnings("UnusedAssignment")
        ResultSet rs = null;
        int count = 0;
        try {
            con = DriverManager.getConnection(url, user, password);
            String query = ("SELECT COUNT(*) FROM `szurasterv` WHERE 1");
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
            ex.printStackTrace(System.err);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
                }
            }
        }
        return count;
    }

    /**
     *
     * @return
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public DB885 readSzurasterv() {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        DB885 db = new DB885();
        try {
            con = DriverManager.getConnection(url, user, password);
            String query = ("SELECT * FROM `dfir`.`szurasterv` WHERE 1 order by `pssschtelid` limit 1");
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            if (rs.next()) {
                System.out.println(new java.util.Date().toString() + " - " + "SELECT * FROM `dfir`.`szurasterv` WHERE 1 order by pssschtelid limit 1");
                MainApp.debug.printDebugMsg(null, SQL.class.getName(), "SELECT * FROM `dfir`.`szurasterv` WHERE 1 order by pssschtelid limit 1");
                try {

                    pasSchID = rs.getInt("pssschtelid");
                    db.DwaPssSchTelId = (short) (pasSchID % 1000);
                    db.CoilId.setMyString(rs.getString("coilid"));
                    try {

                        db.DwaSteelGrade.setMyString(rs.getString("steelgrade"));

                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }
                    try {
                        db.DwaWidth = rs.getFloat("width");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }
                    try {
                        db.DwaThickness = rs.getFloat("thickness");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }
                    try {
                        db.DwaWeight = rs.getFloat("weight") * 1000;
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }
                    try {
                        db.DWACoreDiameter = rs.getFloat("corediameter");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }

                    try {
                        db.DwaExitCoilsNo = rs.getShort("exitcoilsno");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }
                    try {
                        db.DwaLength = (db.DwaWeight / ((db.DwaWidth / 1000) * (db.DwaThickness / 1000)) / 7860);
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }

                    try {
                        db.DwaProcessType = rs.getShort("processtype");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }

                    try {
                        db.DwaElongation = rs.getFloat("elongation");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }

                    try {
                        db.DwaElongLowLim = rs.getFloat("elonglowlim");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }

                    try {
                        db.DwaElongUpLim = rs.getFloat("elonguplim");
                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }

                    try {
                        if (db.DwaExitCoilsNo == 1) {
                            db.DwaExitCoil1Length = 0;
                            db.DwaExitCoil2Length = 0;
                        } else if (db.DwaExitCoilsNo == 2) {
                            db.DwaExitCoil1Length = db.DwaLength / 2;
                            db.DwaExitCoil2Length = db.DwaLength / 2;
                        } else if (db.DwaExitCoilsNo == 3) {
                            db.DwaExitCoil1Length = db.DwaLength / 3;
                            db.DwaExitCoil2Length = db.DwaLength / 3;
                        }

                    } catch (Exception e) {
                        MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", e);
                        e.printStackTrace(System.err);
                    }

                } catch (Exception ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
            db = null;
        } finally {

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }
        }
        return db;
    }

    /**
     *
     * @param db
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public void updateSzurastervLog(DB885 db) {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs;
        String query = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new java.util.Date());
            try {
                query = ("SELECT * FROM `dfir`.`szurasterv_log` WHERE `pssschtelid` = " + pasSchID);

                st = con.prepareStatement(query);
                rs = st.executeQuery();

                if (rs.next()) {
                    System.out.println(new java.util.Date().toString() + " - " + query);
                    MainApp.debug.printDebugMsg(null, SQL.class.getName(), query);
                    date = rs.getString("ts_kuldes");
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                MainApp.debug.printDebugMsg(null, SQL.class.getName(), "SQL error:" + query, ex);
                date = sdf.format(new java.util.Date());
            }
            try {
                query = "UPDATE `szurasterv_log` SET `ts_feldolgozas`='" + date
                        + "' WHERE `pssschtelid`=" + pasSchID;

                System.out.println(new java.util.Date().toString() + " - " + query);
                st = con.prepareStatement(query);
                int result = st.executeUpdate();

                MainApp.debug.printDebugMsg(null, SQL.class.getName(), query);
            } catch (Exception ex) {
                System.out.println(new java.util.Date().toString() + " - " + ex.toString());
                MainApp.debug.printDebugMsg(null, SQL.class.getName(), "SQL error:" + query, ex);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            MainApp.debug.printDebugMsg(null, SQL.class.getName(), "(error) SQL :", ex);
        } finally {

            if (st != null) {
                try {
                    st.close();

                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class
                            .getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

            if (con != null) {
                try {
                    con.close();

                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class
                            .getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }
        }
    }

    /**
     *
     * @param db
     */
    public void writeSzurastervLog(DB885 db) {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            String query = "INSERT INTO `szurasterv_log`"
                    + "( `pssschtelid`,`coilid`, `steelgrade`, `width`, `thickness`, "
                    + "`weight`, `length`, `exitcoilsno`,`exitcoil1length`,`exitcoil2length` ,"
                    + "  `processtype`, `elongation`, `rollforce`,`bendingforce`,`linespeed`,"
                    + "`basicsprayamount`,`tensionporesbr`,`tensionesbrstd`,`tensionstdxsbr`,`tensionxsbrt`,"
                    + "`corediameter`,`elonglowlim`,`elonguplim`,`ts_kuldes`) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            System.out.println(new java.util.Date().toString() + " - " + query);
            st = con.prepareStatement(query);
            st.setInt(1, pasSchID);
            st.setString(2, db.CoilId.getMyString());
            st.setString(3, db.DwaSteelGrade.getMyString());
            st.setFloat(4, db.DwaWidth);
            st.setFloat(5, db.DwaThickness);

            st.setFloat(6, db.DwaWeight);
            st.setFloat(7, db.DwaLength);
            st.setShort(8, db.DwaExitCoilsNo);
            st.setFloat(9, db.DwaExitCoil1Length);
            st.setFloat(10, db.DwaExitCoil2Length);

            st.setShort(11, db.DwaProcessType);
            st.setFloat(12, db.DwaElongation);
            st.setFloat(13, db.DwaRollForce);
            st.setFloat(14, db.DwaBendingForce);
            st.setFloat(15, db.DwaLineSpeed);

            st.setFloat(16, db.DwaBasicSprayAmount);
            st.setFloat(17, db.DwaTensionPorEsBr);
            st.setFloat(18, db.DwaTensionEsBrStd);
            st.setFloat(19, db.DwaTensionStdXsBr);
            st.setFloat(20, db.DwaTensionXsBrT);

            st.setFloat(21, db.DWACoreDiameter);
            st.setFloat(22, db.DwaElongLowLim);
            st.setFloat(23, db.DwaElongUpLim);

            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new java.util.Date());
            st.setString(24, date);

            st.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            MainApp.debug.printDebugMsg(null, SQL.class
                    .getName(), "(error) SQL :", ex);
        } finally {

            if (st != null) {
                try {
                    st.close();

                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class
                            .getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

            if (con != null) {
                try {
                    con.close();

                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class
                            .getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }
        }
    }

    /**
     *
     * @param db
     */
    public void deleteSzurasterv(DB885 db) {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            String query = ("DELETE FROM `dfir`.`szurasterv` WHERE `szurasterv`.`pssschtelid`=?");
            st = con.prepareStatement(query);
            st.setInt(1, pasSchID);
            int deleteCount = st.executeUpdate();
            if (deleteCount > 0) {
                System.out.println(new java.util.Date().toString() + " - " + deleteCount + " sor törölve a szurásterv táblából. tkercsszám: "
                        + db.CoilId.getMyString());
                MainApp.debug.printDebugMsg(null, SQL.class
                        .getName(), deleteCount
                        + " sor törölve a szurásterv táblából. tekercsszám: "
                        + db.CoilId.getMyString());
            }

        } catch (SQLException ex) {
            MainApp.debug.printDebugMsg(null, SQL.class
                    .getName(), "(error) SQL :", ex);
            ex.printStackTrace(System.err);
        } finally {

            if (st != null) {
                try {
                    st.close();

                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class
                            .getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

            if (con != null) {
                try {
                    con.close();

                } catch (SQLException ex) {
                    MainApp.debug.printDebugMsg(null, SQL.class
                            .getName(), "(error) SQL :", ex);
                    ex.printStackTrace(System.err);
                }
            }

        }
    }
}
