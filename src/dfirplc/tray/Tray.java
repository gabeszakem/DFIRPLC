/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.tray;

import static dfirplc.MainApp.DRESSPANELISENABLED;
import static dfirplc.MainApp.FRAMEISENABLED;
import static dfirplc.MainApp.LOGPANELISENABLED;
import static dfirplc.MainApp.LOGVIEWERISENABLED;
import static dfirplc.MainApp.debug;
import static dfirplc.MainApp.dress;
import static dfirplc.MainApp.frame;
import static dfirplc.MainApp.logViewer;
import static dfirplc.MainApp.textAreaLog;
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

            if (FRAMEISENABLED) {
                frame.setVisible(false);
            }
            if (DRESSPANELISENABLED) {
                dress.setVisible(false);
            }
            if (LOGVIEWERISENABLED) {
                logViewer.setVisible(false);
            }
            if (LOGPANELISENABLED) {
                textAreaLog.setVisible(false);
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
            if (FRAMEISENABLED) {
                MenuItem logItem = new MenuItem("table");
                logItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (frame.isVisible()) {
                            frame.setVisible(false);
                        } else {
                            frame.setVisible(true);
                        }

                    }
                });
                popup.add(logItem);
                popup.addSeparator();
            }
            if (DRESSPANELISENABLED) {
                MenuItem dressItem = new MenuItem("dress");
                dressItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (dress.isVisible()) {
                            dress.setVisible(false);
                        } else {
                            dress.setVisible(true);
                        }

                    }
                });
                popup.add(dressItem);
                popup.addSeparator();
            }

            if (LOGPANELISENABLED) {
                MenuItem dressItem = new MenuItem("log");
                dressItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (textAreaLog.isVisible()) {
                            textAreaLog.setVisible(false);
                        } else {
                            textAreaLog.setVisible(true);
                        }

                    }
                });
                popup.add(dressItem);
                popup.addSeparator();
            }

            if (LOGVIEWERISENABLED) {
                MenuItem dressItem = new MenuItem("logviewer");
                dressItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (logViewer.isVisible()) {
                            logViewer.setVisible(false);
                        } else {
                            logViewer.setVisible(true);
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
                    if (FRAMEISENABLED) {
                        if (frame.isVisible()) {
                            frame.dispose();
                        } else {
                            frame.setState(JFrame.NORMAL);
                            frame.setVisible(true);
                        }
                    }
                    if (DRESSPANELISENABLED) {
                        if (dress.isVisible()) {
                            dress.dispose();
                        } else {
                            dress.setState(JFrame.NORMAL);
                            dress.setVisible(true);
                        }
                    }
                    if (LOGPANELISENABLED) {
                        if (textAreaLog.isVisible()) {
                            textAreaLog.dispose();
                        } else {
                            textAreaLog.setState(JFrame.NORMAL);
                            textAreaLog.setVisible(true);
                        }
                    }
                    if (LOGVIEWERISENABLED) {
                        if (textAreaLog.isVisible()) {
                            logViewer.dispose();
                        } else {
                            logViewer.setState(JFrame.NORMAL);
                            logViewer.setVisible(true);
                        }
                    }
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
                debug.printDebugMsg(null, Tray.class.getName(),
                        "Hiba történt a program tálcán futtatásakor", ex);
            }

        } else {
            System.out.println("System tray nem támogatott (" + Tray.class.getSimpleName() + ")");
            debug.printDebugMsg(null, Tray.class.getName(),
                    "System tray nem támogatott");
            frame.setVisible(true);
        }
    }

}
