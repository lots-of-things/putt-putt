package puttingGreen;

import Jama.Matrix;

public class ConstantForce {
	
	double magnitude;
	Matrix direction;

	public ConstantForce(){
		this(0, 0, -1, 9.8);
	}
	
	public ConstantForce(double xDirection, double yDirection, double zDirection, double magnitude){
		this.magnitude = magnitude;
		direction = new Matrix(new double[][]{{xDirection}, {yDirection}, {zDirection}});
		direction = direction.times(1/direction.norm2());
	}
	
	
}
