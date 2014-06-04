package imageCapture;

/*
 * Created on 13-Apr-2005
 *
 * Grab a single frame from the capture device in regular time intervals.
 * Transform the frame into an image (Image, BufferedImage) and paint it inside
 * the panel by overwriting paintComponent() of the JPanel. Also paint the a
 * rotated date string.
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.NoDataSourceException;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.util.BufferToImage;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FrameGrab extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player player = null;
	Timer clock;

	private Vector<BufferedImage> imgVect;

	private FrameGrabbingControl frameGrabber;
	private boolean timeToQuit = false;

	public FrameGrab(Vector<BufferedImage> imgVect){
		this(imgVect, null);
	}
	
	
	@SuppressWarnings("unchecked")
	public FrameGrab(Vector<BufferedImage> imgVect, Player p) {
		// Create capture device
		
		Vector<CaptureDeviceInfo> devices = CaptureDeviceManager.getDeviceList(null);
		this.imgVect = imgVect;
		setPreferredSize(new Dimension(500, 500));
		CaptureDeviceInfo cdi = null;
		devices = CaptureDeviceManager.getDeviceList(null);
		for (Iterator<CaptureDeviceInfo> i = devices.iterator(); i.hasNext();) {
			CaptureDeviceInfo tempcdi = (CaptureDeviceInfo) i.next();
			/* Get the first Video For Windows (VFW) capture device.
			 * Use the JMF registry tool in the bin directory of the JMF
			 * distribution to detect available capture devices on your
			 * computer.
			 */
			
			if (tempcdi.getName().startsWith("vfw:")){
				cdi = tempcdi;
			}
		}
		System.out.println(p);
		clock = new Timer(1000, this);
		if (p==null){
			try {
				DataSource dataSource = Manager.createDataSource(cdi.getLocator());
				player = Manager.createRealizedPlayer(dataSource);
				player.start();
			} catch (NoPlayerException e) {
				e.printStackTrace();
			} catch (CannotRealizeException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoDataSourceException e) {
				e.printStackTrace();
			} 
		}else{
			player = p;
			player.start();
		}
		// Grab a frame from the capture device
		frameGrabber = (FrameGrabbingControl) player
				.getControl("javax.media.control.FrameGrabbingControl");
		//clock.start();
	}
	
	public void setClockRunning(boolean running){
		if(running&&!clock.isRunning()){
			clock.start();
		}
		if(!running&&clock.isRunning()){
			clock.stop();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (imgVect.size() != 0) {
			g.drawImage(imgVect.lastElement(), 0, 0, this);
		}
	}

	public BufferedImage grab() {
		if (frameGrabber != null){
			Buffer buf = frameGrabber.grabFrame();
			// Convert frame to an buffered image so it can be processed and saved
			Image img = (new BufferToImage((VideoFormat) buf.getFormat())
					.createImage(buf));
			if (img != null){
				BufferedImage buffImg = null;
				buffImg = new BufferedImage(img.getWidth(this), img.getHeight(this),
						BufferedImage.TYPE_INT_RGB);
				imgVect.add(buffImg);
				Graphics2D g = buffImg.createGraphics();
				g.drawImage(img, null, null);
				return buffImg;
			}
		}
		return null;
	}

	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if( timeToQuit){
			clock.stop();
		}else{
			grab();
			repaint();
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
		timeToQuit = true;
		frameGrabber = null;
		player.stop();
		//player.close();
		//player.deallocate();
		//player = null;
	}


	public Player getPlayer() {
		// TODO Auto-generated method stub
		return player;
	}
}

