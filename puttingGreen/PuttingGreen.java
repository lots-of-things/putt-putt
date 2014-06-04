package puttingGreen;

import greenViewer.GreenViewer;

import java.util.Vector;

import controlMechanism.ControlMechanism;

import putter.Putter;

public class PuttingGreen {
	Surface puttingSurface;
	SurfacePoint startPoint;
	SurfacePoint cupLocation;
	Vector<ConstantForce> constantForces = new Vector<ConstantForce>();
	double ballMass;
	
	public PuttingGreen(){
		puttingSurface = new Surface(Surface.CENTIMETERS);
		puttingSurface.createClusterPlanes();
	}
	
	public PuttingGreen(SurfacePoint[] inputSurface){
		puttingSurface = new Surface(inputSurface, Surface.CENTIMETERS, 5);
		puttingSurface.createClusterPlanes();
	}
	
	public static void main(String[] args){
//		ControlMechanism control = ControlMechanism.createControlMechanism();
//		GreenViewer g = new GreenViewer(control);
//		g.calibrate();
		double[] points = new double[0];
		PuttingGreen p = new PuttingGreen();
		p.setStartPoint(new SurfacePoint(1, 1, 0));
		p.setCupLocation(new SurfacePoint(20, 20, 0));
		p.addConstantForce(new ConstantForce());
		p.setBallMass(2);
		double[] initialVel = p.findInitialVelocity();
//		Putter putter = new Putter(control);
	}

	private void setBallMass(double i) {
		this.ballMass = i;
	}

	private double[] findInitialVelocity() {
		double x = startPoint.x;
		double y = startPoint.y;
		double xVel = cupLocation.x-startPoint.x;
		double yVel = cupLocation.y-startPoint.y;
		double norm = Math.sqrt(xVel*xVel+yVel*yVel);
		xVel = 5*xVel/norm;
		yVel = 5*yVel/norm;
		return puttingSurface.putt(x, y, xVel, yVel, cupLocation.x, cupLocation.y, constantForces, ballMass);
	}

	private void addConstantForce(ConstantForce constantForce) {
		constantForces.add(constantForce);
	}


	private void setCupLocation(SurfacePoint cupLocation) {
		this.cupLocation = cupLocation;
	}

	private void setStartPoint(SurfacePoint startPoint) {
		this.startPoint = startPoint;
	}
	
	

}
