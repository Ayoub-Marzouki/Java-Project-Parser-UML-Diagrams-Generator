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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/* As far as drawing packages and relationships between classes are concerned :
 * Thinking about following the following approach :
 * Each layer (level of nested folder) will have classes
 * We draw a long non-stoppping line above and below all of these classes
 * We leave some left and right padding
 * Whenever a class is related to another, we link it to the long line, and we give it its proper color, something to distinguish it from other relationships
 * something like this
 */ 

public class Frame extends JFrame {
	private JPanel contentPanel;
	private JScrollPane scrollPane;
	private int contentPanelHeight = 50;
	
	private JLabel label;
	private JButton generate;
	private JTextField userInput;
	
	public Frame() {
		this.setSize(1000, 600);
		this.setTitle("UML Package and Class Diagrams Generator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setLayout(new BorderLayout(10, 20));		
		
		contentPanel = new JPanel();
//		setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		prepareFrame();
		
		this.setVisible(true);
	}
	
	
	/**
	 * Prepares the frame to be set in the following way :
	 * It will be divided based on border layout, and we'll use NORTH and CENTER parts :
	 * NORTH part will be left for prompting user to enter src path of his project.
	 * CENTER part will be used to display the according diagrams.
	 */
	private void prepareFrame() {
//		contentPanel.setLayout(new GridBagLayout());
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

		promptUser();
		setScrollPane();
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
		
		generate.addActionListener(e -> {
			System.out.println("test");
		});
	}
	

	/**
	 * Makes the panel (which will hold the diagrams) scrollable.
	 * Used in : prepareFrame method.
	 */
	private void setScrollPane() {
		scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(scrollPane, BorderLayout.CENTER);
	}
	

	 /**
     *  Adds a component to the content panel of the frame, and refreshes it.
     * 
     * @param c the component to add
     */
	public void addToContentPanel(Component c) {
		contentPanel.add(c);
//		Going for getHeight() doesn't work, but retrieveing it this way does
		contentPanelHeight += c.getPreferredSize().height;
		
		int maxWidth = 0;
		for (Component comp : contentPanel.getComponents()) {
		        maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
		}
//		This is very necessary; the contentPanel's height and width need to be updated everytime a component is added so that the scrollPane takes it into account, otherwise unusual behavior occurs
		contentPanel.setPreferredSize(new Dimension(maxWidth, contentPanelHeight));
		
		this.repaint();
		this.revalidate();
	}
		
//	button getter will be needed to later implement the action it'll perform
	public JButton getButton() {
		return generate;
	}
	
	
	/**	Centers the text inside the rectangle of package name. we calculate each of the horizontal and vertical centering based on the following :
	 * Horizontal centering :
	 * + Width of string (obtained via FontMetrics class)
	 * + Space left after inserting the string inside the rect (width(rect) - width(string))
	 * + starting position of the string x : 
	 * 		we want the center, so free space / 2
	 * 		we then add the x of rectangle
	 * Final formula :
	 * x = x(rectangle) + (width(rect) - with(string)) / 2
	 * 
	 * Vertical Centering :
	 * y = y(rectangle) + (height(rect) - height(string)) / 2 + ascent
	 * 
	 * ascent : the distance from the baseline to the highest position of a character (like T, H...)
	 * 
	 */
	public static void drawCenteredText(Graphics2D g2d, String text, int rectX, int rectY, int rectWidth, int rectHeight) {
	    FontMetrics metrics = g2d.getFontMetrics();
	    int textWidth = metrics.stringWidth(text);
	    int textHeight = metrics.getHeight();

	    // Calculate the center position for the text
	    int stringX = rectX + (rectWidth - textWidth) / 2;
	    int stringY = rectY + (rectHeight - textHeight) / 2 + metrics.getAscent();

	    // Draw the string
	    g2d.drawString(text, stringX, stringY);
	}

}
