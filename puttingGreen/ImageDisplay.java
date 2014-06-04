package puttingGreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

public class ImageDisplay extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	private Vector<Vector<Line2D.Double>> lines;
	
	public ImageDisplay(BufferedImage b, Vector<Vector<Line2D.Double>> lines){
		setPreferredSize(new Dimension(b.getWidth(), b.getHeight()));
		image = b;
		this.lines = lines;
	}
	
	public void paint(Graphics g){
		g.drawImage(image, 0, 0, this);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		for(int i = 0; i < lines.size(); i++){
			g2.setColor(new Color((int)(255*Math.random()), (int)(255*Math.random()), (int)(255*Math.random())));
			for(int j = 0; j < lines.get(i).size(); j++){
				g2.draw(lines.get(i).get(j));
			}
		}
	}
	
}
