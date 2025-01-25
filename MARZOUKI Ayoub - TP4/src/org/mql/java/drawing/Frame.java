package org.mql.java.drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// Check Frame-details.txt for a detailed description

public class Frame extends JFrame {
	private JLayeredPane contentPanel;
	private static JPanel diagramsLayer;
	
	private JScrollPane scrollPane;
//	This will serve as the height for the contentPanel, diagramsLayer and relationsLayer
	private int PanelsHeight = 10;
	
	private JLabel label;
	private static JButton generate;
	private JTextField userInput;
	
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
		contentPanel = new JLayeredPane();
		
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
		
		label = new JLabel("Path to your Source Folder : ");
		label.setBackground(Color.blue);
		
		userInput = new JTextField("path/to/your/srcFolder", 40);
		generate = new JButton("Generate Diagrams");
		
		p.add(label);
		p.add(userInput);
		p.add(generate);
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
	public static JButton getButton() {
		return generate;
	}

	public JTextField getUserInput() {
		return userInput;
	}


	//  This will be used in RelationsLayer to get the index of retrieved packages
	public static JPanel getDiagramsLayer() {
		return diagramsLayer;
	}

	

}
