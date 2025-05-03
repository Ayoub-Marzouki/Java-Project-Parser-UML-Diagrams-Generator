package org.mql.java.swing.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JLayeredPane;

public class ZoomableLayer extends JLayeredPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double zoom = 1.0;

    public void setZoom(double zoom) {
        this.zoom = zoom;
        revalidate();
        repaint();
    }

    public double getZoom() {
        return zoom;
    }

    /**
     * Paints the children components of the container, applying a zoom effect to the graphics.
     * The method scales the graphics before calling the superclass's `paintChildren` method and then restores the original transform.
     * 
     * @param g The Graphics object used to paint the component. It will be cast to a Graphics2D object to apply scaling.
     */
    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();
        g2.scale(zoom, zoom);
        super.paintChildren(g2);
        g2.setTransform(old);
    }

    /**
     * Returns the preferred size of the component, adjusting it based on the zoom factor.
     * 
     * @return A Dimension object representing the preferred size of the component, scaled by the zoom factor.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension orig = super.getPreferredSize();
        return new Dimension(
            (int) (orig.width * zoom),
            (int) (orig.height * zoom)
        );
    }
}
