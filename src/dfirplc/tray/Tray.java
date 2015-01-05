/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tray;

import dfirplc.MainApp;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/**
 *
 * @author gabesz
 */
public class Tray {

    /**
     *
     * @throws Exception
     */
    public Tray() throws Exception {
        final TrayIcon trayIcon;
        SystemTray tray;
        final Image image;
        ActionListener actionListener;
        /*
         * Ellenőrzés, tray icon támogatott?
         */
        if (SystemTray.isSupported()) {

            if (MainApp.FRAMEISENABLED) {
                MainApp.frame.setVisible(false);
            }
            if(MainApp.DRESSPANELISENABLED){
                MainApp.dress.setVisible(false);
            }
            tray = SystemTray.getSystemTray();
            image = Toolkit.getDefaultToolkit().getImage(Tray.class.getResource("/dfirplc/images/testimonials.png"));

            MouseListener mouseListener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }
            };

            ActionListener showListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                }
            };

            PopupMenu popup = new PopupMenu();
            if (MainApp.FRAMEISENABLED) {
                MenuItem logItem = new MenuItem("table");
                logItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (MainApp.frame.isVisible()) {
                            MainApp.frame.setVisible(false);
                        } else {
                            MainApp.frame.setVisible(true);
                        }

                    }
                });
                popup.add(logItem);
                popup.addSeparator();
            }
            if (MainApp.DRESSPANELISENABLED) {
                MenuItem dressItem = new MenuItem("dress");
                dressItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (MainApp.dress.isVisible()) {
                            MainApp.dress.setVisible(false);
                        } else {
                            MainApp.dress.setVisible(true);
                        }

                    }
                });
                popup.add(dressItem);
                popup.addSeparator();
            }
            
            if (MainApp.LOGPANELISENABLED) {
                MenuItem dressItem = new MenuItem("log");
                dressItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (MainApp.textAreaLog.isVisible()) {
                            MainApp.textAreaLog.setVisible(false);
                        } else {
                            MainApp.textAreaLog.setVisible(true);
                        }

                    }
                });
                popup.add(dressItem);
                popup.addSeparator();
            }

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(-1);
                }
            });
            popup.add(exitItem);
            final PopupMenu myPopup = popup;
            trayIcon = new TrayIcon(image, "", myPopup);
            actionListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    /*
                     * trayIcon.displayMessage("Action Event", "",
                     * TrayIcon.MessageType.INFO);
                     */
                    if(MainApp.FRAMEISENABLED){
                    if (MainApp.frame.isVisible()) {
                        MainApp.frame.dispose();
                    } else {
                        MainApp.frame.setState(JFrame.NORMAL);
                        MainApp.frame.setVisible(true);
                    }}
                    if(MainApp.DRESSPANELISENABLED){
                    if (MainApp.dress.isVisible()) {
                        MainApp.dress.dispose();
                    } else {
                        MainApp.dress.setState(JFrame.NORMAL);
                        MainApp.dress.setVisible(true);
                    }}
                    if(MainApp.LOGPANELISENABLED){
                    if (MainApp.textAreaLog.isVisible()) {
                        MainApp.textAreaLog.dispose();
                    } else {
                        MainApp.textAreaLog.setState(JFrame.NORMAL);
                        MainApp.textAreaLog.setVisible(true);
                    }}
                }
            };
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("DFIRPLC");
            trayIcon.addActionListener(actionListener);
            trayIcon.addMouseListener(mouseListener);
            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
                System.out.println("Hiba történt a program tálcán futtatásakor (" + Tray.class.getSimpleName() + ")" + ex.getMessage());
                MainApp.debug.printDebugMsg(null, Tray.class.getName(),
                        "Hiba történt a program tálcán futtatásakor", ex);
            }

        } else {
            System.out.println("System tray nem támogatott (" + Tray.class.getSimpleName() + ")");
            MainApp.debug.printDebugMsg(null, Tray.class.getName(),
                    "System tray nem támogatott");
            MainApp.frame.setVisible(true);
        }
    }

}
