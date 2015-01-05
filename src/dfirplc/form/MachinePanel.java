/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.form;

import dfirplc.db.DB882;
import dfirplc.form.tools.CircleTangents;
import dfirplc.tools.FloatConvert;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author gabesz
 */
public class MachinePanel extends javax.swing.JPanel {

    /**
     * Creates new form MachinePanel
     */
    public MachinePanel() {
        initComponents();
    }
    
    /**
     *
     * @param db882
     */
    public void createPanel(DB882 db882) {
        this.db882 = db882;
    }
    
    private void doDrawing(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        Dimension size = getSize();
        Insets insets = getInsets();
        
        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;
        int b = insets.bottom;
        int t = insets.top;
        int l = insets.left;
        
        int centerPosition = h / 2;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.BLACK);
        /**
         * Görgők megrajzolása
         */
        float coilSugar = w / 10;
        float distance = w / 80;
        float brSugar = w / 30;
        float fmrSugar = w / 50;
        float mdSugar = w / 30;
        float burSugar = w / 12;
        
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv;
        Rectangle2D rc;

        /* Felcsévélő tekerccsel*/
        Ellipse2D.Double tr = coil(g2d, coilSugar + distance, centerPosition, coilSugar, "TR");
        /* Kilépő oldali felső S görgő */
        Ellipse2D.Double xsbrTop = roll(g2d, (float) tr.getMaxX() + brSugar + 2 * distance, centerPosition + brSugar, brSugar, "XsT");
        /* Kilépő oldali alsó S görgő */
        Ellipse2D.Double xsbrBottom = roll(g2d, (float) xsbrTop.getCenterX(), (float) xsbrTop.getMaxY() + brSugar, brSugar, "XsB");
        /* Síkkifekvésmérő görgő */
        Ellipse2D.Double fmr = roll(g2d, (float) xsbrTop.getMaxX() + distance + fmrSugar, (float) centerPosition, fmrSugar, "Fmr");
        /* Felső főhajtás motor */
        Ellipse2D.Double mdTop = roll(g2d, (float) fmr.getMaxX() + (6 * distance) + mdSugar,
                (float) fmr.getMinY() - mdSugar, mdSugar, "MdT");
        /* Alsó főhajtás motor */
        Ellipse2D.Double mdBottom = roll(g2d, (float) mdTop.getCenterX(),
                (float) mdTop.getMaxY() + mdSugar, mdSugar, "MdB");
        /* Felső támhenger */
        Ellipse2D.Double BURTop = roll(g2d, (float) mdTop.getCenterX(),
                (float) mdTop.getMinY() - burSugar, burSugar, "", Color.GRAY);
        /* Alsó támhenger */
        Ellipse2D.Double BURBottom = roll(g2d, (float) mdBottom.getCenterX(),
                (float) mdBottom.getMaxY() + burSugar, burSugar, "", Color.gray);
        /* Belépő oldali felső S görgő */
        Ellipse2D.Double esbrTop = roll(g2d, (float) mdTop.getMaxX() + brSugar + (10 * distance),
                (float) fmr.getMinY() + brSugar, brSugar, "EsT");
        /* Belépő oldali alsó S görgő */
        Ellipse2D.Double esbrBottom = roll(g2d, (float) esbrTop.getCenterX(), (float) esbrTop.getMaxY() + brSugar, brSugar, "EsB");
        /* Lelcsévélő tekerccsel*/
        Ellipse2D.Double por = coil(g2d, w - (coilSugar + distance), centerPosition, coilSugar, "POR");

        /**
         * Lemez rajzolása
         */
        g2d.setPaint(Color.black);

        /* Felcsévélő - Kilépő alsó S görgő egyenes*/
        double[][] tangentsTrXsBr = tangents(tr, xsbrBottom);
        drawLine(g2d, tangentsTrXsBr, 0);

        /*  Kilépő alsó S görgő */
        Double degreeXsbrBottom = degree(xsbrBottom, tangentsTrXsBr[0][2]);
        g2d.drawArc((int) xsbrBottom.getMinX(), (int) xsbrBottom.getMinY(),
                (int) xsbrBottom.getWidth(), (int) xsbrBottom.getHeight(), (int) (-90 - degreeXsbrBottom), (int) (180 + degreeXsbrBottom));

        /* Kilépő felső S görgő - Síkkifekvésmérő egyenes */
        double[][] tangentsXsBrFmr = tangents(xsbrTop, fmr);
        drawLine(g2d, tangentsXsBrFmr, 1);

        /* Kilépő felső S görgő */
        Double degreeXsbrTop = degree(xsbrTop, tangentsXsBrFmr[1][0]);
        g2d.drawArc((int) xsbrTop.getMinX(), (int) xsbrTop.getMinY(),
                (int) xsbrTop.getWidth(), (int) xsbrTop.getHeight(), (int) (90 + degreeXsbrTop), (int) (180 - degreeXsbrTop));

        /* Síkkifekvésmérő */
        Double degreefmr = degree(fmr, tangentsXsBrFmr[1][2]);
        g2d.drawArc((int) fmr.getMinX(), (int) fmr.getMinY(),
                (int) fmr.getWidth(), (int) fmr.getHeight(), 90, (int) (double) (degreefmr));

        /* Síkkifekvésmérő  - Belépő felső S görgő egyenes */
        double[][] tangentsfmrEsbr = tangents(fmr, esbrTop);
        drawLine(g2d, tangentsfmrEsbr, 1);

        /* Belépő felső S görgő */
        Double degreeEsBrTop = degree(esbrTop, tangentsfmrEsbr[1][2]);
        g2d.drawArc((int) esbrTop.getMinX(), (int) esbrTop.getMinY(),
                (int) esbrTop.getWidth(), (int) esbrTop.getHeight(), -90, (int) (180 + degreeEsBrTop));

        /* Belépő felső S görgő - Lecsévélő egyenes */
        double[][] tangentsEsbrPor = tangents(esbrBottom, por);
        drawLine(g2d, tangentsEsbrPor, 0);

        /* Belépő alsó S görgő */
        Double degreeEsBrBottom = degree(esbrBottom, tangentsEsbrPor[0][0]);
        g2d.drawArc((int) esbrBottom.getMinX(), (int) esbrBottom.getMinY(),
                (int) esbrBottom.getWidth(), (int) esbrBottom.getHeight(), 90, (int) (180 + degreeEsBrBottom));

        /**
         * Feliratok adatok
         */
        /*db882.CoilId.setMyString("0100111");
         db882.ElongRef = 1;
         db882.ElongAct = 1.09f;
         db882.RollingSpeed = 1000;
         db882.TrRollStripLength = 12300;*/

        /*record */
        drawRecord(g2d, w, l, t);

        /*Tekercsazonosító*/
        drawCoilNumber(g2d, frc, t, l, tr);

        /*Nyúlás*/
        drawElogation(g2d, frc, t, l, tr);
        /*Hossz*/
        drawLength(g2d, frc, t, l, tr);
        /*Sebesség*/
        drawSpeed(g2d, frc, t, l, tr);
        
    }
    
    private void drawLength(Graphics2D g2d, FontRenderContext frc, int t, int l, Ellipse2D.Double tr) {
        GlyphVector gv;
        Rectangle2D rc;
        /*Hengerlési hossz*/
        g2d.setPaint(Color.BLACK);
        float f = FloatConvert.convertTo2Digits(db882.TrRollStripLength);
        g2d.setPaint(Color.red);
        gv = g2d.getFont().createGlyphVector(frc, "Hossz:");
        rc = gv.getPixelBounds(null, t, l);
        g2d.drawString("Hossz:", (float) (tr.getCenterX() - rc.getWidth() / 2), (float) (tr.getMaxY() + rc.getHeight() * 1.5 + 2));
        gv = g2d.getFont().createGlyphVector(frc, Float.toString(f));
        rc = gv.getPixelBounds(null, t, l);
        g2d.setPaint(Color.BLACK);
        g2d.drawString(Float.toString(f), (float) (tr.getCenterX() - rc.getWidth() / 2), (float) (tr.getMaxY() + rc.getHeight() * 3));
    }
    
    private void drawSpeed(Graphics2D g2d, FontRenderContext frc, int t, int l, Ellipse2D.Double tr) {
        GlyphVector gv;
        Rectangle2D rc;
        /*Hengerlési sebesség*/
        g2d.setPaint(Color.BLACK);
        float f;
        if (run) {
            f = FloatConvert.convertTo2Digits(db882.RollingSpeed);
        } else {
            f = 0f;
        }
        g2d.setPaint(Color.red);
        gv = g2d.getFont().createGlyphVector(frc, "Sebesség:");
        rc = gv.getPixelBounds(null, t, l);
        g2d.drawString("Sebesség:", (float) (tr.getMaxX()), (float) (tr.getMinY() - rc.getHeight() * 2.5) - 4);
        gv = g2d.getFont().createGlyphVector(frc, Float.toString(f));
        rc = gv.getPixelBounds(null, t, l);
        g2d.setPaint(Color.BLACK);
        g2d.drawString(Float.toString(f), (float) (tr.getMaxX()), (float) (tr.getMinY() - rc.getHeight() * 2.5));
    }
    
    private void drawRecord(Graphics2D g2d, int w, int l, int t) {
        
        if (db882.TelegrId != previousTelegramId) {
            timestamp = System.currentTimeMillis();
        }
        if ((System.currentTimeMillis() - timestamp) < 5000) {
            run=true;
            if (blink) {
                Ellipse2D.Double record = roll(g2d, w - l - 8, 8, 6, "", Color.RED);
                g2d.setPaint(Color.RED);
                g2d.fill(record);
                blink = false;
            } else {
                blink = true;
            }
        } else {
            run=false;
            g2d.fill(new Rectangle2D.Double(w - l - 14, t + 2, 12, 12));
        }
        previousTelegramId = db882.TelegrId;
    }
    
    private void drawCoilNumber(Graphics2D g2d, FontRenderContext frc, int t, int l, Ellipse2D.Double tr) {
        GlyphVector gv;
        Rectangle2D rc;
        g2d.setPaint(Color.BLACK);
        gv = g2d.getFont().createGlyphVector(frc, db882.CoilId.getMyString());
        rc = gv.getPixelBounds(null, t, l);
        g2d.drawString(db882.CoilId.getMyString(), (float) (tr.getCenterX() - rc.getWidth() / 2),
                (float) (tr.getMinY() - rc.getHeight() / 2));
        g2d.setPaint(Color.red);
        gv = g2d.getFont().createGlyphVector(frc, "Tekercs");
        rc = gv.getPixelBounds(null, t, l);
        g2d.drawString("Tekercs:", (float) (tr.getCenterX() - rc.getWidth() / 2),
                (float) (tr.getMinY() - rc.getHeight() * 1.5) - 2);
    }
    
    private void drawElogation(Graphics2D g2d, FontRenderContext frc, int t, int l, Ellipse2D.Double tr) {
        GlyphVector gv;
        Rectangle2D rc;
        
        float eloScale = 0.1f;
        float eloMax = db882.ElongRef * (1 + eloScale);
        float eloMin = db882.ElongRef * (1 - eloScale);
        float eloWidth = eloMax - eloMin;
        float elopercent;
        if (eloMax > db882.ElongAct) {
            elopercent = (db882.ElongAct - eloMin) / eloWidth;
        } else if (eloMin > db882.ElongAct) {
            elopercent = 0;
        } else {
            elopercent = 1;
        }
        g2d.setPaint((Color.red));
        float f;
        if (run) {
            f = FloatConvert.convertTo2Digits(db882.ElongAct);
        } else {
            f = 0f;
        }
        gv = g2d.getFont().createGlyphVector(frc, "Nyúlás:" + "1.55");
        rc = gv.getPixelBounds(null, t, l);
        g2d.drawString("Nyúlás:" + f, (float) (tr.getMaxX()),
                (float) (tr.getMinY() - rc.getHeight() / 2));
        
        g2d.setPaint(Color.green);
        g2d.fill(new Rectangle2D.Double(tr.getMaxX(), tr.getMinY() - rc.getHeight() / 2 + 4,
                rc.getWidth() * elopercent, rc.getHeight()));
        Rectangle2D.Double elo = new Rectangle2D.Double(tr.getMaxX(), tr.getMinY() - rc.getHeight() / 2 + 4, rc.getWidth(), rc.getHeight());
        g2d.setPaint(Color.BLACK);
        g2d.draw(elo);
        g2d.setPaint(Color.red);
        g2d.drawLine((int) (tr.getMaxX() + (rc.getWidth() * 0.5)), (int) (tr.getMinY() - rc.getHeight() / 2 + 4),
                (int) (tr.getMaxX() + (rc.getWidth() * 0.5)), (int) (tr.getMinY() + rc.getHeight() / 2 + 4));
        
    }
    
    private void drawLine(Graphics2D g2d, double[][] tangents, int select) {
        g2d.drawLine((int) tangents[select][0], (int) tangents[select][1], (int) tangents[select][2], (int) tangents[select][3]);
    }
    
    private Double degree(Ellipse2D.Double circle, double x) {
        return Math.toDegrees(Math.tan(Math.abs(circle.getCenterX() - x) / (circle.width / 2)));
    }
    
    private double[][] tangents(Ellipse2D.Double circle1, Ellipse2D.Double circle2) {
        return CircleTangents.getTangents(circle1.getCenterX(), circle1.getCenterY(), circle1.getWidth() / 2,
                circle2.getCenterX(), circle2.getCenterY(), circle2.getWidth() / 2);
    }
    
    private Ellipse2D.Double coil(Graphics2D g2d, float x, float y, float sugar, String text) {
        Ellipse2D.Double coil = null;
        roll(g2d, x, y, sugar / 2, text);
        float step = 5;
        for (float index = sugar / 2; index <= sugar;) {
            coil = roll(g2d, x, y, index);
            index += step;
        }
        return coil;
    }
    
    private Ellipse2D.Double roll(Graphics2D g2d, float x, float y, float sugar, String text) {
        float diameter = sugar * 2;
        if (run) {
            g2d.setPaint(Color.GREEN);
        } else {
            g2d.setPaint(Color.BLUE);
        }
        Ellipse2D.Double circle = new Ellipse2D.Double(x - sugar, y - sugar, diameter, diameter);
        g2d.fill(circle);
        float cx = (float) circle.getCenterX();
        float cy = (float) circle.getCenterY();
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = g2d.getFont().createGlyphVector(frc, text);
        Rectangle2D rc = gv.getPixelBounds(null, x, y);
        g2d.setPaint(Color.black);
        g2d.drawString(text, cx - ((float) rc.getWidth() / 2), cy + ((float) rc.getHeight() / 2));
        return circle;
    }
    
    private Ellipse2D.Double roll(Graphics2D g2d, float x, float y, float sugar, String text, Color color) {
        float diameter = sugar * 2;
        g2d.setPaint(color);
        Ellipse2D.Double circle = new Ellipse2D.Double(x - sugar, y - sugar, diameter, diameter);
        g2d.draw(circle);
        float cx = (float) circle.getCenterX();
        float cy = (float) circle.getCenterY();
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = g2d.getFont().createGlyphVector(frc, text);
        Rectangle2D rc = gv.getPixelBounds(null, x, y);
        g2d.setPaint(Color.black);
        g2d.drawString(text, cx - ((float) rc.getWidth() / 2), cy + ((float) rc.getHeight() / 2));
        return circle;
    }
    
    private Ellipse2D.Double roll(Graphics2D g2d, float x, float y, float sugar) {
        float diameter = sugar * 2;
        g2d.setPaint(Color.BLACK);
        Ellipse2D.Double circle = new Ellipse2D.Double(x - sugar, y - sugar, diameter, diameter);
        g2d.draw(circle);
        return circle;
    }
    
    /**
     *A panel frissítése.
     */
    public void refreshPanels() {
        this.repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private DB882 db882;
    private static boolean blink = false;
    private static boolean run=false;
    private static short previousTelegramId = 0;
    private static long timestamp = System.currentTimeMillis();
}
