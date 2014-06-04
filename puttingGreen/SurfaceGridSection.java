package puttingGreen;

import java.util.Vector;

import Jama.Matrix;


public class SurfaceGridSection {
	
	SurfacePoint surfacePoint;
	private boolean containsSurfacePoint;
	public Integer[] nearestSurfacePointGridLocation = new Integer[2];
	private double distanceToNearestSurfacePoints;
	double zValue;
	boolean hit = false;
	
	public SurfaceGridSection(SurfacePoint surfacePoint) {
		// TODO Auto-generated constructor stub
		this.containsSurfacePoint = true;
		distanceToNearestSurfacePoints = 0;
		this.surfacePoint = surfacePoint;
	}
	
	public SurfaceGridSection() {
		this.containsSurfacePoint = false;
		distanceToNearestSurfacePoints = Integer.MAX_VALUE;
	}

	public boolean containsSurfacePoint(){
		return containsSurfacePoint;
	}

	public boolean setNearestPoint(int i, int j, double smallestDistance) {
		// TODO Auto-generated method stub
		//System.out.println("OK");
		if (smallestDistance < this.distanceToNearestSurfacePoints){
			this.distanceToNearestSurfacePoints = smallestDistance;
			nearestSurfacePointGridLocation = new Integer[] {i, j};
			return true;
		}
		return false;
	}

	public double getDistanceToNearestSurfacePoint() {
		// TODO Auto-generated method stub
		return distanceToNearestSurfacePoints;
	}

	public SurfacePoint getSurfacePoint() {
		// TODO Auto-generated method stub
		return surfacePoint;
	}

	public void setPlaneZValue(double value) {
		// TODO Auto-generated method stub
		zValue = value;
	}

	public double getZValue() {
		// TODO Auto-generated method stub
		return zValue;
	}

	public void setGridLocation(int gridLocationX, int gridLocationY) {
		// TODO Auto-generated method stub
		nearestSurfacePointGridLocation = new Integer[]{gridLocationX, gridLocationY};
		surfacePoint.setGridLocation(gridLocationX, gridLocationY);
	}

	public Matrix putt(double x, double y, double vel, double vel2) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
