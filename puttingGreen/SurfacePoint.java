package puttingGreen;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.Vector;

import Jama.Matrix;

public class SurfacePoint {

	double x, y, z;
	double[] components;
	Vector<SurfacePoint> nearbyPoints = new Vector<SurfacePoint>();
	int pointsToCollect;
	int xLocationOnGrid, yLocationOnGrid;
	Matrix surroundingPlane;
	private Matrix totalForce;
	private double ballMass;
	
	public SurfacePoint (double x, double y, double z){
		this(x, y, z, 5);
	}
	
	public SurfacePoint (double[] points){
		this(points[0], points[1], points[2], 5);
	}
	
	public SurfacePoint (double x, double y, double z, int desiredNumberOfPointsInSurfaceSegment){
		this.x = x;
		this.y = y;
		this.z = z;
		components = new double[] {x, y, z};
		pointsToCollect = desiredNumberOfPointsInSurfaceSegment;
	}
	
	public SurfacePoint(){
		this(Math.random()*25, Math.random()*25, Math.random()*25);
	}

	public int addNearbyPoint(SurfacePoint surfacePoint) {
		if(nearbyPoints.size()<pointsToCollect){
			for(int i = 0; i < nearbyPoints.size(); i++){
				if(nearbyPoints.get(i).equals(surfacePoint)){
					return pointsToCollect - nearbyPoints.size();
				}
			}
			nearbyPoints.add(surfacePoint);
			if(nearbyPoints.size()==pointsToCollect){
				double[][] checkArr = new double[nearbyPoints.size()][4];
				for(int i = 0; i < nearbyPoints.size(); i++){
					for(int j = 0; j < 4; j++){
						checkArr[i][j] = nearbyPoints.get(i).getComponent(j);
					}
				}
				Matrix check = new Matrix(checkArr);
				Matrix transpose = check.transpose();
				Matrix normalCheck = transpose.times(check);
				Matrix nullMatrix = new Matrix(4, 1);
				try{
					Matrix checkSolution = normalCheck.solve(nullMatrix);
				}catch (Exception e){
					nearbyPoints.remove(surfacePoint);
				}
				
			}
			return pointsToCollect - nearbyPoints.size();
		}
		return 0;
	}
	public int getNumberOfPointsNeeded() {
		// TODO Auto-generated method stub
		return pointsToCollect - nearbyPoints.size();
	}
	public Vector<SurfacePoint> getNearbyPoints() {
		// TODO Auto-generated method stub
		return nearbyPoints;
	}
	public void setGridLocation(int gridLocationX, int gridLocationY) {
		// TODO Auto-generated method stub
		xLocationOnGrid = gridLocationX;
		yLocationOnGrid = gridLocationY;
	}
	
	
	public void setNormalVector() {
		// TODO Auto-generated method stub
		int numberOfTerms = 4;
		Matrix matrix = new Matrix(nearbyPoints.size(), numberOfTerms-1);
		for(int i = 0; i < nearbyPoints.size(); i++){
			for(int j  = 0; j < numberOfTerms-1; j++){
				matrix.set(i, j, nearbyPoints.get(i).getComponent(j));
			}
			matrix.set(i, 2, nearbyPoints.get(i).getComponent(3));
		}
		Matrix transpose = matrix.transpose();
		Matrix normalMatrix = transpose.times(matrix);
		Matrix zMatrix = new Matrix(nearbyPoints.size(), 1);
		for(int i = 0; i < nearbyPoints.size(); i++){
			zMatrix.set(i, 0, nearbyPoints.get(i).getComponent(2));
		}
		Matrix zNorm = transpose.times(zMatrix);
		//normalMatrix.print(2, 1);
		//System.out.println();
		Matrix solution = normalMatrix.solve(zNorm);
		//solution.print(2, 1);
		surroundingPlane = solution;
	}
	
	
	private double getComponent(int index) {
		if(index < components.length){
			return components[index];
		} else{
			double twoNorm = 0;
			for(int i = 0; i < components.length; i ++){
				twoNorm+=components[i];
			}
			return Math.sqrt(twoNorm);
		}
	}
	public double computeZValue(double xCenter, double yCenter) {
		double zValue = xCenter*surroundingPlane.get(0, 0)+yCenter*surroundingPlane.get(1, 0) + surroundingPlane.get(2, 0);
			//System.out.println(surroundingPlane.get(0, 0));
		return zValue;
	}
	public double computeZVelocity(double xVel, double yVel) {
		double zVel = xVel*surroundingPlane.get(0, 0)+yVel*surroundingPlane.get(1, 0);
		return zVel;
	}
	public double[] getK() {
		// TODO Auto-generated method stub
		double ax = 7*(1+Math.pow(surroundingPlane.get(0,0), 2))/5;
		double ay = 7*(1+Math.pow(surroundingPlane.get(1,0), 2))/5;
		double b = 7*surroundingPlane.get(0,0)*surroundingPlane.get(1,0)/5;
		double gx = (totalForce.get(0, 0)+totalForce.get(2, 0)*surroundingPlane.get(0,0));
		double gy = (totalForce.get(1, 0)+totalForce.get(2, 0)*surroundingPlane.get(1,0));
		double kx = (ay*gx-b*gy)/(ax*ay-b*b);
		double ky = (gy-b*kx)/ay;
		//DecimalFormat df = new DecimalFormat("0.00");
		//System.out.println(df.format(surroundingPlane.get(0,0))+ " "+df.format(surroundingPlane.get(1,0))+ " "+df.format(totalForce.norm2())+" "+df.format(totalForce.get(0,0))+ " "+df.format(totalForce.get(1,0))+ " "+df.format(totalForce.get(2,0)));
		//System.out.println(df.format(ax)+" "+df.format(ay)+" "+df.format(b)+" "+df.format(gx)+" "+df.format(gy)+" "+df.format(kx)+" "+df.format(ky)+" ");
		return new double [] {kx, ky};
	}
	
	public void createPotential(Vector<ConstantForce> constantForces) {
		// TODO Auto-generated method stub
		totalForce =  new Matrix(3, 1);
		for(int i = 0; i < constantForces.size(); i++){
			totalForce = constantForces.get(i).direction.times(constantForces.get(i).magnitude);
		}
		
	}
	public void inputBallMass(double ballMass) {
		// TODO Auto-generated method stub
		this.ballMass = ballMass;
	}
	

	
}
