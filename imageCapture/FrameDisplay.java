package imageCapture;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

public class FrameDisplay extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector<BufferedImage> bufIms;
	
	public FrameDisplay(Vector<BufferedImage> bufIms){
		this.bufIms = bufIms;
		setPreferredSize(new Dimension(bufIms.get(0).getWidth(), bufIms.get(0).getHeight()*bufIms.size()));
	}
	
	public void paint(Graphics g){
		super.paint(g);
		for(int i = 0; i < bufIms.size(); i ++){
			int ydisp = bufIms.get(i).getHeight();
			g.drawImage(bufIms.get(i), 0, 0, this);
			g.translate(0, ydisp);
		}
	}
	
}
