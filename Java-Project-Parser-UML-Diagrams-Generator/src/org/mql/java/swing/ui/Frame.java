package org.mql.java.swing.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

// Check Frame-details.txt for a detailed description

public class Frame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ZoomableLayer contentPanel;
	private static JPanel diagramsLayer;
	
	private JScrollPane scrollPane;
//	This will serve as the height for the contentPanel, diagramsLayer and relationsLayer
	private int PanelsHeight = 10;
	
	private JLabel label;
	private JButton generate;
	private JTextField userInput;
	
	private static JDialog popup;
    private static JLabel statusLabel;
	
	public Frame() {
		this.setSize(1000, 600);
		this.setTitle("UML Package and Class Diagrams Generator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout(10, 20));		
		
//		setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		prepareFrame();
		
		this.setVisible(true);
	}
	
	
	/**
	 * <h1>Prepares the frame to be set in the following way :</h1> </br>
	 * It will be divided based on border layout, and we'll use NORTH and CENTER parts : </br>
	 * <strong>NORTH</strong> part will be left for prompting user to enter src path of his project. </br>
	 * <strong>CENTER</strong> part will be used to display the according diagrams. </br>
	 */
	private void prepareFrame() {
		diagramsLayer = new JPanel();
		contentPanel = new ZoomableLayer();
		
//		A placeholder for any potential problems (e.g. user enters an invalid src folder path)
		popup = new JDialog(this, "Error", true); 
		popup.setSize(500, 120);
		popup.setLocationRelativeTo(this);
		
		statusLabel = new JLabel(" ");
		statusLabel.setOpaque(true);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    statusLabel.setForeground(Color.red);
	    
	    popup.add(statusLabel);
	    		
//		If we set some other layout to the layered pane, it'll override the way layeredpanes work, so it has to be set to null
		contentPanel.setLayout(null);
//		Set some space between the diagramLayer panel and its container the contentPanel
		diagramsLayer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));


		promptUser();
		setScrollPane();
		
		contentPanel.add(diagramsLayer, Integer.valueOf(1));
		contentPanel.setBackground(Color.red);
//		contentPanel.add(relationsLayer, 2);
	}
	
	/**
	 * Creates a label, a text field where user can enter the path to his project's src folder, and a button that he can click on to generate the appropriate diagrams.
	 * Used in : prepareFrame() method
	 */
	private void promptUser() {
		JPanel p = new JPanel();
		
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("–");

        zoomIn.addActionListener(e -> {
            double z = contentPanel.getZoom();
            contentPanel.setZoom(Math.min(z + 0.1, 3.0));
        });
        zoomOut.addActionListener(e -> {
            double z = contentPanel.getZoom();
            contentPanel.setZoom(Math.max(z - 0.05, 0.001));
        });
		
		label = new JLabel("Path to your Source Folder : ");
		label.setBackground(Color.blue);
		
		userInput = new JTextField("path/to/your/srcFolder", 40);
		generate = new JButton("Generate Diagrams");
		
		p.add(label);
		p.add(userInput);
		p.add(generate);
		
		p.add(zoomOut);
		p.add(zoomIn);
		this.add(p, BorderLayout.NORTH);
		
	}

	/**
	 * Makes the contentPanel (which will hold the diagrams) scrollable. </br>
	 * Used in : prepareFrame method.
	 */
	private void setScrollPane() {
		scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Updates the status bar at the bottom of the frame.
	 */
	public static void setStatus(String message) {
	    statusLabel.setText(message);
	}

	

	 /**
     *  Adds a component to the content panel of the frame, and refreshes it; updates dynamically the width/height of diagramsLayer by taking everything into account; from components' width & height to the layout used's gaps
     * 
     * @param 
     * Component c :  the component to add
     */
	public void addToDiagramsLayer(Component c) {
		diagramsLayer.add(c);
//		Going for getHeight() doesn't work, but retrieveing it this way does
		PanelsHeight += c.getPreferredSize().height + ((FlowLayout) diagramsLayer.getLayout()).getVgap();

		
		int maxWidth = 0;
		for (Component comp : diagramsLayer.getComponents()) {
		        maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
		}
//		This is very necessary; the panels' height and width need to be updated everytime a component is added so that the scrollPane takes it into account, otherwise unusual behavior occurs
		contentPanel.setPreferredSize(new Dimension(maxWidth, PanelsHeight));
//		Using setBounds instead of PreferredSize is necessary because of contentPanel's layout
		diagramsLayer.setBounds(0, 0, maxWidth, PanelsHeight);
		
		this.repaint();
		this.revalidate();
	}
	
	
	/**
	 * Adds the {@link RelationsLayer} on top of the diagrams layer within the content panel.
	 * <p>
	 * This method sets the layout, bounds, and transparency of the {@code RelationsLayer}, then
	 * adds it to the {@code contentPanel} at the specified Z-order to ensure it is layered correctly.
	 * Setting the layer to be non-opaque is crucial to allow visibility of the diagrams layer beneath it.
	 *
	 * @param rl the {@code RelationsLayer} instance to be added to the content panel
	 */
	public void addRelationsLayer(RelationsLayer rl) {
		rl.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
		rl.setBounds(0, 0, diagramsLayer.getWidth(), diagramsLayer.getHeight());
//		Also necessary otherwise the diagramlayer (below this layer) won't be visible
		rl.setOpaque(false);

//		Using a primitive int won't work because add method's overloaded version in LayeredPane expects an object and not a primitive
		contentPanel.add(rl, Integer.valueOf(2));
		contentPanel.revalidate();
		contentPanel.repaint();
	}
		
//	button getter will be needed to later implement the action it'll perform
	public  JButton getButton() {
		return generate;
	}

	public JTextField getUserInput() {
		return userInput;
	}


	//  This will be used in RelationsLayer to get the index of retrieved packages
	public static JPanel getDiagramsLayer() {
		return diagramsLayer;
	}
	
	
//	This will be used in ProjectProcessor to show the error
	public static JDialog getPopup() {
		return popup;
	}

	

}
