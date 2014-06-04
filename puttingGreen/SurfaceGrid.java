package puttingGreen;

import java.text.DecimalFormat;
import java.util.Vector;

import Jama.Matrix;

public class SurfaceGrid {

	private SurfaceGridSection[][] surfaceGrid;
	private int size;
	double xGridCenters[];
	double yGridCenters[];
	private VisualGridPoint[][] displayGrid;
	private double cupX;
	private double cupY;
	
	public SurfaceGrid (Surface s){
		this.size = s.size();
		surfaceGrid = new SurfaceGridSection[size()][size()];
		xGridCenters = new double[size()];
		yGridCenters = new double[size()];
		for(int i = 0; i < surfaceGrid.length; i++){
			for(int j = 0; j < surfaceGrid.length; j++){
				surfaceGrid[i][j] = new SurfaceGridSection();
			}
		}
		SurfacePoint[] xOrdering = new SurfacePoint[size()];
		SurfacePoint[] yOrdering = new SurfacePoint[size()];
		for (int i = 0; i < size(); i++){
			xOrdering[i] = s.get(i);
			yOrdering[i] = s.get(i);
		}
		for(int i = 1; i < xOrdering.length; i++){
			for(int j = 0; j < xOrdering.length-i; j++){
				if(xOrdering[j].x>xOrdering[j+1].x){
					SurfacePoint temp = xOrdering[j];
					xOrdering[j]=xOrdering[j+1];
					xOrdering[j+1]=temp;
				}
				if(yOrdering[j].y>yOrdering[j+1].y){
					SurfacePoint temp = yOrdering[j];
					yOrdering[j]=yOrdering[j+1];
					yOrdering[j+1]=temp;
				}
			}
		}
		for(int i = 0; i < size(); i++){
			xGridCenters[i] = xOrdering[i].x;
			yGridCenters[i] = yOrdering[i].y;
		}
		
		for(int i = 0; i < size(); i++){
			int xGridLocation = -1;
			int yGridLocation = -1;
			for(int j = 0; j < size(); j++){
				if(s.get(i).equals(xOrdering[j])){
					xGridLocation = j;
				}
				if(s.get(i).equals(yOrdering[j])){
					yGridLocation = j;
				}
			}
			
			if(xGridLocation !=-1&&yGridLocation!=-1){
				addPointToGrid(s.get(i), xGridLocation, yGridLocation);
			}else{
				System.out.println("That's not gonna work");
			}
		}
		setUpNearbyPoints();
	}
	
	

	private void setUpNearbyPoints() {
		for(int i = 0; i < size() ; i++){
			for(int j = 0; j < size() ; j++){
				if(getGridSection(i, j).containsSurfacePoint()){
					int xDown = 0;
					int xUp = 0;
					int yDown = 0;
					int yUp = 0;
					int practicalxDown = 0;
					int practicalxUp = 0;
					int practicalyDown = 0;
					int practicalyUp = 0;
					
					
					while(getGridSection(i, j).getSurfacePoint().getNumberOfPointsNeeded()>0){
						double nextNearestStepLength= Double.MAX_VALUE;
						int nextNearestStepDirectionInt = -1;
						if(i>xDown&&(xGridCenters[i]-xGridCenters[i-xDown-1]<nextNearestStepLength)){
							nextNearestStepDirectionInt = 0;
						}else
						if(i+xUp<size()-1&&(xGridCenters[i+xUp+1]-xGridCenters[i]<nextNearestStepLength)){
							nextNearestStepDirectionInt = 1;
						}else
						if(j>yDown&&(yGridCenters[j]-yGridCenters[j-yDown-1]<nextNearestStepLength)){
							nextNearestStepDirectionInt = 2;
						}else
						if(j+yUp<size()-1&&(yGridCenters[j+yUp +1]-yGridCenters[j]<nextNearestStepLength)){
							nextNearestStepDirectionInt = 3;
						}else{
							
						}
						if(nextNearestStepDirectionInt == 0){
							xDown++;
							practicalxDown = xDown;
							practicalxUp = -xDown;
							practicalyDown = yDown;
							practicalyUp = yUp;
						}else
						if(nextNearestStepDirectionInt == 1){
							xUp++;
							practicalxDown = -xUp;
							practicalxUp = xUp;
							practicalyDown = yDown;
							practicalyUp = yUp;
						}else
						if(nextNearestStepDirectionInt == 2){
							yDown++;
							practicalxDown = xDown;
							practicalxUp = xUp;
							practicalyDown = yDown;
							practicalyUp = -yDown;
						}else
						if(nextNearestStepDirectionInt == 3){
							yUp++;
							practicalxDown = xDown;
							practicalxUp = xUp;
							practicalyDown = -yUp;
							practicalyUp = yUp;
						}else{
							System.out.println("What the fuck");
						}
						for(int ii = i-practicalxDown; ii <= i+practicalxUp ; ii++){
							for(int jj = j - practicalyDown; jj <= j + practicalyUp ; jj++){
								if(ii!=i&&jj!=j&&getGridSection(ii, jj).containsSurfacePoint()){
									getGridSection(i, j).getSurfacePoint().addNearbyPoint(getGridSection(ii, jj).getSurfacePoint());
								}
								getGridSection(ii, jj).setNearestPoint(i, j, Math.sqrt(Math.pow(xGridCenters[ii]-xGridCenters[i], 2 )+Math.pow(yGridCenters[jj]-yGridCenters[j], 2 )));
							}
						}
					}
				}
				
			}
		}	
	}

	public void printStuff(){
//		for(int i = 0; i < surfaceGrid.length; i++){
//			for(int j = 0; j < surfaceGrid.length; j++){
//				System.out.print(surfaceGrid[i][j].getDistanceToNearestSurfacePoint()+" ");
//			}
//			System.out.println();
//		}
//		System.out.println();
	}
	
	public int size(){
		return this.size;
	}

	private void addPointToGrid(SurfacePoint surfacePoint, int gridLocationX, int gridLocationY) {
		// TODO Auto-generated method stub
		surfaceGrid[gridLocationX][gridLocationY] = new SurfaceGridSection(surfacePoint);
		surfaceGrid[gridLocationX][ gridLocationY].setGridLocation(gridLocationX, gridLocationY);
	}

	public SurfaceGridSection getGridSection(int i, int j) {
		//if (i >= 0&& j>= 0&& i < size()&& j < size()){
			return surfaceGrid[i][j];
		//}
		//return new SurfaceGridSection();
	}

	public void buildPlanes() {
		// TODO Auto-generated method stub
		for(int i = 0 ; i < size(); i++){
			for(int j = 0 ; j < size(); j ++){
				double zValue = 0;
				double xCenter = getXCenter(i);
				double yCenter = getYCenter(j);
				zValue= getGridSection(getGridSection(i, j).nearestSurfacePointGridLocation[0], getGridSection(i, j).nearestSurfacePointGridLocation[1]).getSurfacePoint().computeZValue(xCenter, yCenter);
				getGridSection(i, j).setPlaneZValue(zValue);
			}
		}
	}

	private double getYCenter(int j) {
		
		return yGridCenters[j];
	}

	private double getXCenter(int i) {
		// TODO Auto-generated method stub
		return xGridCenters[i];
	}
	
	public void generateVisualGrid(int xDimension, int yDimension){
		displayGrid = new VisualGridPoint[xDimension][yDimension];
		double xStep = (xGridCenters[xGridCenters.length-1]-xGridCenters[0])/(xDimension-1);
		double yStep = (yGridCenters[yGridCenters.length-1]-yGridCenters[0])/(yDimension-1);
		for(int i = 0; i < displayGrid.length; i++){
			for(int j = 0; j < displayGrid[i].length; j++){
				displayGrid[i][j] = new VisualGridPoint();
				Vector<Integer> xCoords = new Vector<Integer>();
				Vector<Integer> yCoords = new Vector<Integer>();
				for(int ii = 0 ; ii < size(); ii++){
					if(xGridCenters[ii]>=i*xStep+xGridCenters[0]&&xGridCenters[ii]<(i+1)*xStep+xGridCenters[0]){
						xCoords.add(ii);
					}
				}
				for(int jj = 0 ; jj < size(); jj++){
					if(yGridCenters[jj]>=j*yStep+yGridCenters[0]&&yGridCenters[jj]<(j+1)*yStep+yGridCenters[0]){
						yCoords.add(jj);
					}
				}
				for(int ii = 0; ii < xCoords.size(); ii++){
					for(int jj = 0; jj < yCoords.size(); jj++){
						displayGrid[i][j].add(surfaceGrid[xCoords.get(ii)][yCoords.get(jj)]);
					}
				}
			}
		}
		
	}

	public int getDisplayXSize(){
		return displayGrid.length;
	}
	
	public int getDisplayYSize(){
		return displayGrid[0].length;
	}

	public VisualGridPoint getVisualGridPoint(int i, int j) {
		
		return displayGrid[i][j];
	}



	public double[] putt(double x, double y, double xVel, double yVel) {
		//System.out.println("in surfaceGrid putt");
		int xGridLow = 0;
		double xLow = 0;
		double xHigh = 0;
		int yGridLow = 0;
		double yLow = 0;
		double yHigh = 0;
		for(int i = 0; i < xGridCenters.length; i++){
			if(xGridCenters[i]>x){
				xHigh = xGridCenters[i];
				break;
			}
			if(xGridCenters[i]!=xLow){
				xLow = xGridCenters[i];
				xGridLow = i;
			}
		}
		for(int i = 0; i < yGridCenters.length; i++){
			if(yGridCenters[i]>y){
				yHigh = yGridCenters[i];
				break;
			}
			if(yGridCenters[i]!=yLow){
				yLow = yGridCenters[i];
				yGridLow = i;
			}
		}
		if(yHigh == 0||xHigh == 0||yHigh == yGridCenters[0]|| xHigh == xGridCenters[0]){
			return new double[]{-2, x, y, 0, 0, 0, 0};
		}
		if(xGridLow == 0 || yGridLow == 0){
			return new double[]{-2, x, y, 0, 0, 0, 0};
		}
		DecimalFormat df = new DecimalFormat("0.00");
		//System.out.println("putt " + df.format(xGridLow) + " " +df.format(yGridLow) + " " +df.format(x) + " " +df.format(y) + " " +df.format(xVel) + " " +df.format(yVel) );
		
		SurfacePoint currentSurfacePoint = getGridSection(getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[0], getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[1]).getSurfacePoint();
		double[] k = currentSurfacePoint.getK();
		double kx = k[0];
		double ky = k[1];
		double tXLow = solveT(kx, xVel, xLow, x);
		double tYLow = solveT(ky, yVel, yLow, y);
		double tXHigh = solveT(kx, xVel, xHigh, x);
		double tYHigh = solveT(ky, yVel, yHigh, y);
		//System.out.println(df.format(tXLow)+" "+df.format(tYLow)+" "+df.format(tXHigh)+" "+df.format(tYHigh)+" ");
		
		double lowTime = Double.MAX_VALUE;
		
		if(tXLow > 0 && tXLow < lowTime){
			lowTime = tXLow;
		}
		if(tYLow > 0 && tYLow < lowTime){
			lowTime = tYLow;
		}
		if(tXHigh > 0 && tXHigh < lowTime){
			lowTime = tXHigh;
		}
		if(tYHigh > 0 && tYHigh < lowTime){
			lowTime = tYHigh;
		}
		if(lowTime == Double.MAX_VALUE){
			x-=.005;
			y-=.005;
			return putt(x, y, xVel, yVel);
		}
		if(tXLow == lowTime){
			return composeReturnMatrix(xGridLow, yGridLow, xLow-100*Double.MIN_VALUE, solveS(ky, tXLow, yVel, y), solveV(kx, tXLow, xVel), solveV(ky, tXLow, yVel));//left(xGridLow, yGridLow, xLow, solveS(ky, tXLow, yVel, y), solveV(kx, tXLow, xVel), solveV(ky, tXLow, yVel), distanceToCup);
		}else
		if(tYLow == lowTime){
			return composeReturnMatrix(xGridLow, yGridLow, solveS(kx, tXLow, xVel, x), yLow-100*Double.MIN_VALUE, solveV(kx, tYLow, xVel), solveV(ky, tYLow, yVel));//return down(xGridLow, yGridLow, solveS(kx, tYLow, xVel, x), yLow, solveV(kx, tYLow, xVel), solveV(ky, tYLow, yVel), distanceToCup);
		}else
		if(tXHigh == lowTime){
			return composeReturnMatrix(xGridLow, yGridLow, xHigh+Double.MIN_VALUE, solveS(ky, tXHigh, yVel, y), solveV(kx, tXHigh, xVel), solveV(ky, tXHigh, yVel));//return right(xGridLow, yGridLow, xHigh, solveS(ky, tXHigh, yVel, y), solveV(kx, tXHigh, xVel), solveV(ky, tXHigh, yVel), distanceToCup);
		}else
		if(tYHigh == lowTime){
			return composeReturnMatrix(xGridLow, yGridLow, solveS(kx, tYHigh, xVel, x), yHigh+Double.MIN_VALUE, solveV(kx, tYHigh, xVel), solveV(ky, tYHigh, yVel));//return up(xGridLow, yGridLow, solveS(kx, tYHigh, xVel, x), yHigh, solveV(kx, tYHigh, xVel), solveV(ky, tYHigh, yVel), distanceToCup);
		}
		
		return new double[]{-1, x, y};
	}

	private double[] composeReturnMatrix(double xGrid, double yGrid, double x, double y, double xVel,	double yVel) {
		// TODO Auto-generated method stub
		return new double[] {1, x, y, xVel, yVel, xGrid, yGrid};
	}



	private Matrix up(int xGrid, int yGrid, double x, double y, double xVel, double yVel, double distanceToCup) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("up " + df.format(xGrid) + " " +df.format(yGrid) + " " +df.format(x) + " " +df.format(y) + " " +df.format(xVel) + " " +df.format(yVel) + " " + df.format(distanceToCup));
		
		if(Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2))>distanceToCup){
			return new Matrix(new double[][]{{distanceToCup},{cupX-x}, {cupY-y}});
		}
		distanceToCup = Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2));
		int xGridLow = xGrid;
		boolean searching = true;
		int yGridLow = yGrid;
		double yLow = y;
		int i = yGridLow;
		while(searching){
			if(i > yGridCenters.length){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(yGridCenters[i]>=yLow){
				yGridLow = i;
				yLow = yGridCenters[i];
				searching = false;
			}
			i++;
		}
		double xLow = xGridCenters[xGridLow];
		double xHigh = 0;
		double yHigh = 0;
		i = xGridLow;
		while(searching){
			if(xGridCenters[i]>xLow){
				xHigh = xGridCenters[i];
				searching = false;
			}
			i++;
		}
		i = yGridLow;
		while(searching){
			if(i > yGridCenters.length){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(yGridCenters[i]>yLow){
				yHigh = yGridCenters[i];
				searching = false;
			}
			i++;
		}
		searching = true;
		
		
		SurfacePoint currentSurfacePoint = getGridSection(getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[0], getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[1]).getSurfacePoint();
		double[] k = currentSurfacePoint.getK();
		double kx = k[0];
		double ky = k[1];
		double tXLow = solveT(kx, xVel, xLow, x);
		double tYLow = solveT(ky, yVel, yLow, y);
		double tXHigh = solveT(kx, xVel, xHigh, x);
		double tYHigh = solveT(ky, yVel, yHigh, y);
		System.out.println(df.format(tXLow)+" "+df.format(tYLow)+" "+df.format(tXHigh)+" "+df.format(tYHigh)+" ");
		double lowTime = Double.MAX_VALUE;
		if(tXLow > 0 && tXLow < lowTime){
			lowTime = tXLow;
		}
		if(tYLow > 0 && tYLow < lowTime){
			lowTime = tYLow;
		}
		if(tXHigh > 0 && tXHigh < lowTime){
			lowTime = tXHigh;
		}
		if(tYHigh > 0 && tYHigh < lowTime){
			lowTime = tYHigh;
		}
		if(lowTime == Double.MAX_VALUE){
			return new Matrix(3, 1);
		}
		if(tXLow == lowTime){
			return left(xGridLow, yGridLow, xLow, solveS(ky, tXLow, yVel, y), solveV(kx, tXLow, xVel), solveV(ky, tXLow, yVel), distanceToCup);
		}else
		if(tYLow == lowTime){
			return down(xGridLow, yGridLow, solveS(kx, tYLow, xVel, x), yLow, solveV(kx, tYLow, xVel), solveV(ky, tYLow, yVel), distanceToCup);
		}else
		if(tXHigh == lowTime){
			return right(xGridLow, yGridLow, xHigh, solveS(ky, tXHigh, yVel, y), solveV(kx, tXHigh, xVel), solveV(ky, tXHigh, yVel), distanceToCup);
		}else
		if(tYHigh == lowTime){
			return up(xGridLow, yGridLow, solveS(kx, tYHigh, xVel, x), yHigh, solveV(kx, tYHigh, xVel), solveV(ky, tYHigh, yVel), distanceToCup);
		}
		return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
	}
	
	private Matrix down(int xGrid, int yGrid, double x, double y,	double xVel, double yVel, double distanceToCup) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("down " + df.format(xGrid) + " " +df.format(yGrid) + " " +df.format(x) + " " +df.format(y) + " " +df.format(xVel) + " " +df.format(yVel) + " " + df.format(distanceToCup));
		if(Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2))>distanceToCup){
			return new Matrix(new double[][]{{distanceToCup},{cupX-x}, {cupY-y}});
		}
		distanceToCup = Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2));
		int xGridLow = xGrid;
		double yHigh = yGridCenters[yGrid];
		boolean searching = true;
		int yGridLow = yGrid;
		double yLow = yGridCenters[yGrid-1];
		int i = yGrid-1;
		while(searching){
			if(i < 0){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(yGridCenters[i]<yLow){
				yGridLow = i+1;
				yLow = yGridCenters[i+1];
				searching = false;
			}
			i--;
		}
		double xLow = xGridCenters[xGridLow];
		double xHigh = 0;
		i = xGridLow;
		searching = true;
		while(searching){
			if(i > xGridCenters.length){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(xGridCenters[i]>xLow){
				xHigh = xGridCenters[i];
				searching = false;
			}
			i++;
		}
		
		
		SurfacePoint currentSurfacePoint = getGridSection(getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[0], getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[1]).getSurfacePoint();
		double[] k = currentSurfacePoint.getK();
		double kx = k[0];
		double ky = k[1];
		double tXLow = solveT(kx, xVel, xLow, x);
		double tYLow = solveT(ky, yVel, yLow, y);
		double tXHigh = solveT(kx, xVel, xHigh, x);
		double tYHigh = solveT(ky, yVel, yHigh, y);
		double lowTime = Double.MAX_VALUE;
		System.out.println(df.format(tXLow)+" "+df.format(tYLow)+" "+df.format(tXHigh)+" "+df.format(tYHigh)+" ");
		
		if(tXLow > 0 && tXLow < lowTime){
			lowTime = tXLow;
		}
		if(tYLow > 0 && tYLow < lowTime){
			lowTime = tYLow;
		}
		if(tXHigh > 0 && tXHigh < lowTime){
			lowTime = tXHigh;
		}
		if(tYHigh > 0 && tYHigh < lowTime){
			lowTime = tYHigh;
		}
		if(lowTime == Double.MAX_VALUE){
			return null;
		}
		if(tXLow == lowTime){
			return left(xGridLow, yGridLow, xLow, solveS(ky, tXLow, yVel, y), solveV(kx, tXLow, xVel), solveV(ky, tXLow, yVel), distanceToCup);
		}else
		if(tYLow == lowTime){
			return down(xGridLow, yGridLow, solveS(kx, tYLow, xVel, x), yLow, solveV(kx, tYLow, xVel), solveV(ky, tYLow, yVel), distanceToCup);
		}else
		if(tXHigh == lowTime){
			return right(xGridLow, yGridLow, xHigh, solveS(ky, tXHigh, yVel, y), solveV(kx, tXHigh, xVel), solveV(ky, tXHigh, yVel), distanceToCup);
		}else
		if(tYHigh == lowTime){
			return up(xGridLow, yGridLow, solveS(kx, tYHigh, xVel, x), yHigh, solveV(kx, tYHigh, xVel), solveV(ky, tYHigh, yVel), distanceToCup);
		}
		return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
	}
	
	private Matrix left(int xGrid, int yGrid, double x, double y,	double xVel, double yVel, double distanceToCup) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("left " + df.format(xGrid) + " " +df.format(yGrid) + " " +df.format(x) + " " +df.format(y) + " " +df.format(xVel) + " " +df.format(yVel) + " " + df.format(distanceToCup));
		if(Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2))>distanceToCup){
			return new Matrix(new double[][]{{distanceToCup},{cupX-x}, {cupY-y}});
		}
		distanceToCup = Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2));
		int yGridLow = yGrid;
		double xHigh = xGridCenters[xGrid];
		boolean searching = true;
		int xGridLow = xGrid;
		double xLow = xGridCenters[xGrid-1];
		int i = xGrid-1;
		while(searching){
			if(i < 0){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(xGridCenters[i]<xLow){
				xGridLow = i+1;
				xLow = xGridCenters[i+1];
				searching = false;
			}
			i--;
		}
		double yLow = yGridCenters[yGridLow];
		double yHigh = 0;
		i = yGridLow;
		searching = true;
		while(searching){
			if(i > yGridCenters.length){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(yGridCenters[i]>yLow){
				yHigh = yGridCenters[i];
				searching = false;
			}
			i++;
		}
		
		SurfacePoint currentSurfacePoint = getGridSection(getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[0], getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[1]).getSurfacePoint();
		double[] k = currentSurfacePoint.getK();
		double kx = k[0];
		double ky = k[1];
		double tXLow = solveT(kx, xVel, xLow, x);
		double tYLow = solveT(ky, yVel, yLow, y);
		double tXHigh = solveT(kx, xVel, xHigh, x);
		double tYHigh = solveT(ky, yVel, yHigh, y);
		System.out.println(df.format(tXLow)+" "+df.format(tYLow)+" "+df.format(tXHigh)+" "+df.format(tYHigh)+" ");
		double lowTime = Double.MAX_VALUE;
		if(tXLow > 0 && tXLow < lowTime){
			lowTime = tXLow;
		}
		if(tYLow > 0 && tYLow < lowTime){
			lowTime = tYLow;
		}
		if(tXHigh > 0 && tXHigh < lowTime){
			lowTime = tXHigh;
		}
		if(tYHigh > 0 && tYHigh < lowTime){
			lowTime = tYHigh;
		}
		if(lowTime == Double.MAX_VALUE){
			
		}
		if(tXLow == lowTime){
			return left(xGridLow, yGridLow, xLow, solveS(ky, tXLow, yVel, y), solveV(kx, tXLow, xVel), solveV(ky, tXLow, yVel), distanceToCup);
		}else
		if(tYLow == lowTime){
			return down(xGridLow, yGridLow, solveS(kx, tYLow, xVel, x), yLow, solveV(kx, tYLow, xVel), solveV(ky, tYLow, yVel), distanceToCup);
		}else
		if(tXHigh == lowTime){
			return right(xGridLow, yGridLow, xHigh, solveS(ky, tXHigh, yVel, y), solveV(kx, tXHigh, xVel), solveV(ky, tXHigh, yVel), distanceToCup);
		}else
		if(tYHigh == lowTime){
			return up(xGridLow, yGridLow, solveS(kx, tYHigh, xVel, x), yHigh, solveV(kx, tYHigh, xVel), solveV(ky, tYHigh, yVel), distanceToCup);
		}
		return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
	}
	
	private Matrix right(int xGrid, int yGrid, double x, double y, double xVel, double yVel, double distanceToCup) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("right " + df.format(xGrid) + " " +df.format(yGrid) + " " +df.format(x) + " " +df.format(y) + " " +df.format(xVel) + " " +df.format(yVel) + " " + df.format(distanceToCup));
		if(Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2))>distanceToCup){
			return new Matrix(new double[][]{{distanceToCup},{cupX-x}, {cupY-y}});
		}
		distanceToCup = Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2));
		int yGridLow = yGrid;
		boolean searching = true;
		int xGridLow = xGrid;
		double xLow = x;
		int i = xGridLow;
		while(searching){
			if(i > xGridCenters.length){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(xGridCenters[i]>=xLow){
				xGridLow = i;
				xLow = xGridCenters[i];
				searching = false;
			}
			i++;
		}
		double yLow = yGridCenters[yGridLow];
		double yHigh = 0;
		double xHigh = 0;
		i = yGridLow;
		while(searching){
			if(i > yGridCenters.length){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(yGridCenters[i]>yLow){
				yHigh = yGridCenters[i];
				searching = false;
			}
			i++;
		}
		i = xGridLow;
		while(searching){
			if(i > xGridCenters.length){
				return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
			}
			if(xGridCenters[i]>xLow){
				xHigh = xGridCenters[i];
				searching = false;
			}
			i++;
		}
		
		SurfacePoint currentSurfacePoint = getGridSection(getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[0], getGridSection(xGridLow, yGridLow).nearestSurfacePointGridLocation[1]).getSurfacePoint();
		double[] k = currentSurfacePoint.getK();
		double kx = k[0];
		double ky = k[1];
		double tXLow = solveT(kx, xVel, xLow, x);
		double tYLow = solveT(ky, yVel, yLow, y);
		double tXHigh = solveT(kx, xVel, xHigh, x);
		double tYHigh = solveT(ky, yVel, yHigh, y);
		System.out.println(df.format(tXLow)+" "+df.format(tYLow)+" "+df.format(tXHigh)+" "+df.format(tYHigh)+" ");
		
		double lowTime = Double.MAX_VALUE;
		if(tXLow > 0 && tXLow < lowTime){
			lowTime = tXLow;
		}
		if(tYLow > 0 && tYLow < lowTime){
			lowTime = tYLow;
		}
		if(tXHigh > 0 && tXHigh < lowTime){
			lowTime = tXHigh;
		}
		if(tYHigh > 0 && tYHigh < lowTime){
			lowTime = tYHigh;
		}
		if(lowTime == Double.MAX_VALUE){
			
		}
		if(tXLow == lowTime){
			return left(xGridLow, yGridLow, xLow, solveS(ky, tXLow, yVel, y), solveV(kx, tXLow, xVel), solveV(ky, tXLow, yVel), distanceToCup);
		}else
		if(tYLow == lowTime){
			return down(xGridLow, yGridLow, solveS(kx, tYLow, xVel, x), yLow, solveV(kx, tYLow, xVel), solveV(ky, tYLow, yVel), distanceToCup);
		}else
		if(tXHigh == lowTime){
			return right(xGridLow, yGridLow, xHigh, solveS(ky, tXHigh, yVel, y), solveV(kx, tXHigh, xVel), solveV(ky, tXHigh, yVel), distanceToCup);
		}else
		if(tYHigh == lowTime){
			return up(xGridLow, yGridLow, solveS(kx, tYHigh, xVel, x), yHigh, solveV(kx, tYHigh, xVel), solveV(ky, tYHigh, yVel), distanceToCup);
		}
		return new Matrix(new double[][]{{-1},{cupX-x}, {cupY-y}});
	}




	private double solveV(double k, double t, double v) {
		// TODO Auto-generated method stub
		return k*t+v;
	}



	

	private double solveS(double k, double t, double v, double s0) {
		// TODO Auto-generated method stub
		return k*Math.pow(t, 2)/2+v*t+s0;
	}



	private double solveT(double ks, double sVel, double s, double s0) {
		double firstTerm = sVel/ks;
		DecimalFormat df = new DecimalFormat("0.00");
		//System.out.println(df.format(ks) + " " + df.format(sVel)+ " "+ df.format(s)+ " "+df.format(s0));
		double inside = Math.pow(firstTerm, 2) + 2*(s-s0)/ks;
		if(inside<0){
			return -1;
		}
		double retVal = Double.MAX_VALUE;
		if(Math.sqrt(inside)-firstTerm>=0 &&Math.sqrt(inside)-firstTerm<retVal){
			retVal = Math.sqrt(inside)-firstTerm;
		}
		if(-Math.sqrt(inside)-firstTerm>=0 &&-Math.sqrt(inside)-firstTerm<retVal){
			retVal = -Math.sqrt(inside)-firstTerm;
		}
		if(retVal == Double.MAX_VALUE){
			return -1;
		}
		return retVal;
	}

	public void setCupLocation(double cupX, double cupY) {
		// TODO Auto-generated method stub
		this.cupX = cupX;
		this.cupY = cupY;
	}



	public void setPointsHit(Vector<Vector<Integer[]>> pointsHit) {
		// TODO Auto-generated method stub
		for(int i = 0; i < pointsHit.size(); i++){
			for(int j = 0; j < pointsHit.get(i).size(); j++){
				surfaceGrid[pointsHit.get(i).get(j)[0]][pointsHit.get(i).get(j)[1]].hit = true;
			}
		}
	}

	
}
