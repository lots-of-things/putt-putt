package greenViewer;

import java.awt.image.BufferedImage;
import java.util.Vector;

import controlMechanism.ControlMechanism;

import imageCapture.FrameGrab;

public class GreenViewer {
	Vector<BufferedImage> imgVect = new Vector<BufferedImage>();
	FrameGrab grabber;
	LaserControl laserControl;
	CameraPositionControl cameraControl;
	
	public GreenViewer(ControlMechanism control){
		grabber = new FrameGrab(imgVect);
		laserControl = new LaserControl(control);
		cameraControl = new CameraPositionControl(control);
	}

	public void calibrate() {
		// TODO Auto-generated method stub
		BufferedImage b = grabber.grab();
		
	}
	
	public double[] spotBall(){
		return null;
	}
	
	public double[] spotCup(){
		return null;
	}
	
	public double[] checkGravity(){
		return null;
	}
	
	public double[][] surveySurface(){
		
		
		 
		
		
		
		return null;
	}
	
}
