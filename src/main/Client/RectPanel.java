package main.Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Simple class that contains a rectangle that we should be able to
 * manipulate.
 * @author David Kramer
 *
 */
public class RectPanel extends JPanel {

	private Rectangle rect;
	
	public RectPanel() {
		rect = new Rectangle(50, 50, 100, 100);
		setBorder(new LineBorder(Color.CYAN));
		setBackground(Color.BLACK);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.CYAN);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		paintBorder(g);
		g.dispose();
	}
	
	public Rectangle getRect() {
		return rect;
	}
}
