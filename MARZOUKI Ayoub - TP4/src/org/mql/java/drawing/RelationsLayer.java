package org.mql.java.drawing;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.mql.java.drawing.relations.cls.Aggregation;

public class RelationsLayer extends JPanel {
	 private List<Aggregation> aggregations = new ArrayList<>();

	    public void addAggregation(Aggregation agg) {
	        aggregations.add(agg);
	        repaint(); // Trigger a repaint to draw the new aggregation
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        for (Aggregation agg : aggregations) {
	            agg.draw(g);
	        }
	    }
	
//	Utilities
	public static void rotate() {
		
	}

}
