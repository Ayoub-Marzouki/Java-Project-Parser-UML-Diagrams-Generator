package org.mql.java.swing.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JLayeredPane;

public class ZoomableLayer extends JLayeredPane {
    private double zoom = 1.0;

    public void setZoom(double zoom) {
        this.zoom = zoom;
        revalidate();
        repaint();
    }

    public double getZoom() {
        return zoom;
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();
        g2.scale(zoom, zoom);
        super.paintChildren(g2);
        g2.setTransform(old);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension orig = super.getPreferredSize();
        return new Dimension(
            (int) (orig.width * zoom),
            (int) (orig.height * zoom)
        );
    }
}
