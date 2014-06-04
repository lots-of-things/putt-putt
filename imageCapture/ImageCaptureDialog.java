package imageCapture;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Vector;

import javax.media.Player;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageCaptureDialog extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector<BufferedImage> imgVect;
	JPanel panel;
	JScrollPane grabberPane;
	FrameGrab grabber;
	FrameDisplay display;
	Player p;
	
	JButton button1, button2;
	
	public ImageCaptureDialog(){
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		imgVect = new Vector<BufferedImage>();
		
		grabber = new FrameGrab(imgVect);
		grabberPane = new JScrollPane(grabber);
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new GridLayout(1, 2));
		button1 = new JButton("do your stuff");
		button1.addActionListener(this);
		butPanel.add(button1);
		button2 = new JButton("reset");
		button2.addActionListener(this);
		butPanel.add(button2);
		panel.add(butPanel);
		panel.add(grabberPane);
		
		this.add(panel);
		pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		grabber.setClockRunning(true);
	}
	
	public static  void main(String[] args){
		new ImageCaptureDialog();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(button1)){
			((FrameGrab) grabber).stop();
			p = ((FrameGrab) grabber).getPlayer();
			System.out.println();
			Vector difImg = ImageDifferenceFinder.findDifferences(imgVect);
			display = new FrameDisplay(difImg);
			panel.remove(grabberPane);
			grabberPane = new JScrollPane(display);
			panel.add(grabberPane);
			panel.validate();
			repaint();
		}else if(e.getSource().equals(button2)){
			System.out.print(p.toString());
			grabber = new FrameGrab(imgVect, p);
			panel.remove(grabberPane);
			imgVect.removeAllElements();
			grabberPane = new JScrollPane(grabber);
			panel.add(grabberPane);
			panel.validate();
			grabber.setClockRunning(true);
			repaint();
		}
	}
}
