package boundary;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class test 
{
	public static void main(String[] args)
	{
		JPanel mainPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Gradient background
				GradientPaint gradient = new GradientPaint(
					0, 0, new Color(255, 250, 245),
					0, getHeight(), new Color(248, 248, 252)
				);
				g2.setPaint(gradient);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		mainPanel.setSize(600, 400);
		JFrame f = new JFrame();
		f.setSize(650, 430);
		f.add(mainPanel);
		f.setVisible(true);
		
		mainPanel.add(new JLabel("CIAOOO"));
		mainPanel.setVisible(true);
	}
} 
