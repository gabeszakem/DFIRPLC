/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfirplc.form;

import dfirplc.db.DB882;
import dfirplc.tools.FloatConvert;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 *
 * @author gkovacs02
 */
public class StressometerPanel extends javax.swing.JPanel {

    /**
     * Creates new form StressometerPanel
     */
    public StressometerPanel() {
        initComponents();
    }

    private void doDrawing(Graphics g) {

        float[] db882Values = {db882.FlatDevZone1, db882.FlatDevZone2, db882.FlatDevZone3, db882.FlatDevZone4,
            db882.FlatDevZone5, db882.FlatDevZone6, db882.FlatDevZone7, db882.FlatDevZone8,
            db882.FlatDevZone9, db882.FlatDevZone10, db882.FlatDevZone11, db882.FlatDevZone12,
            db882.FlatDevZone13, db882.FlatDevZone14, db882.FlatDevZone15, db882.FlatDevZone16,
            db882.FlatDevZone17, db882.FlatDevZone18, db882.FlatDevZone19, db882.FlatDevZone20,
            db882.FlatDevZone21, db882.FlatDevZone22, db882.FlatDevZone23, db882.FlatDevZone24,
            db882.FlatDevZone25, db882.FlatDevZone26, db882.FlatDevZone27, db882.FlatDevZone28,
            db882.FlatDevZone29, db882.FlatDevZone30, db882.FlatDevZone31, db882.FlatDevZone32};
        Arrays.sort(db882Values);
        minimum = db882Values[0];
        maximum = db882Values[db882Values.length - 1];

        if (maximum > Math.abs(minimum)) {
            limit = maximum * 1.1f;
        } else {
            limit = Math.abs(minimum) * 1.1f;
        }
        if (limit < LIMITMIN) {
            limit = LIMITMIN;
        }

        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize();
        Insets insets = getInsets();

        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;
        int b = insets.bottom;
        int t = insets.top;
        int l = insets.left;

        float[] data = {db882.FlatDevZone1, db882.FlatDevZone2, db882.FlatDevZone3, db882.FlatDevZone4,
            db882.FlatDevZone5, db882.FlatDevZone6, db882.FlatDevZone7, db882.FlatDevZone8,
            db882.FlatDevZone9, db882.FlatDevZone10, db882.FlatDevZone11, db882.FlatDevZone12,
            db882.FlatDevZone13, db882.FlatDevZone14, db882.FlatDevZone15, db882.FlatDevZone16,
            db882.FlatDevZone17, db882.FlatDevZone18, db882.FlatDevZone19, db882.FlatDevZone20,
            db882.FlatDevZone21, db882.FlatDevZone22, db882.FlatDevZone23, db882.FlatDevZone24,
            db882.FlatDevZone25, db882.FlatDevZone26, db882.FlatDevZone27, db882.FlatDevZone28,
            db882.FlatDevZone29, db882.FlatDevZone30, db882.FlatDevZone31, db882.FlatDevZone32};

        int centerPosition = h / 2;
        float barWidth = w / 32;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int idx = data.length - 1; idx >= 0; idx--) {
            g2d.setPaint(Color.blue);
            fill(g2d, idx * barWidth, centerPosition, barWidth, data[idx]);
            g2d.setPaint(Color.LIGHT_GRAY);
            drawLine(g2d, idx * barWidth, h, t);
            if (idx == 0 || (32 - idx) % 5 == 0 || idx == 31) {
                g2d.setPaint(Color.BLACK);
                g2d.drawString(Integer.toString(32 - idx), idx * barWidth, h - 2);
            }
        }
        int newWidth = (int) (data.length * barWidth);

        g2d.setPaint(Color.gray);
        g2d.drawLine(l, t, newWidth, t);
        g2d.drawLine(l, h - 1, newWidth, h - 1);
        g2d.drawLine(l, h, l, t);
        g2d.drawLine(newWidth, h, newWidth, t);
        g2d.setPaint(Color.black);
        g2d.drawLine(l, h / 2, newWidth, h / 2);

        g2d.setPaint(Color.RED);
        String s = "Síkkifekvés Diagram";
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gv = g2d.getFont().createGlyphVector(frc, s);
        Rectangle2D rc = gv.getPixelBounds(null, t, l);
        g2d.drawString(s, l + 4, t + 4 + (int) rc.getHeight());
        g2d.drawString(Float.toString(FloatConvert.convertTo2Digits(limit)), l + 4, t + 4 + (int) (2 * rc.getHeight()));

    }

    /**
     *
     * @param db882
     */
    public void createPanels(DB882 db882) {
        this.db882 = db882;

    }

    private void drawLine(Graphics2D g2d, float startPosition, int h, int t) {
        g2d.drawLine((int) startPosition, h, (int) startPosition, t);
    }

    private void fill(Graphics2D g2d, float startPosition, int center, float width, float value) {
        float barHight = value * center / limit;
        if (barHight > 0.0f) {
            g2d.fill(new Rectangle2D.Double(startPosition, center - barHight, width, Math.abs(barHight)));
        } else {
            g2d.fill(new Rectangle2D.Double(startPosition, center, width, Math.abs(barHight)));
        }
    }

    /**
     *A panel frissitése
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

        setBackground(new java.awt.Color(255, 255, 255));

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
    private float maximum = 0;
    private float minimum = 0;
    private float limit = 0;
    private final static float LIMITMIN = 5f;

}
