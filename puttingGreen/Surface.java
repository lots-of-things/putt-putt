package puttingGreen;

import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Jama.Matrix;

public class Surface extends Vector<SurfacePoint>{
	public static final int CENTIMETERS = 1;
	public static final int INCHES = 2;
	int unitSize;
	SurfaceGrid surfaceGrid;
	Vector<Vector<Integer[]>> pointsHit;
	
	public Surface(int unitSize){
		this.unitSize = unitSize;
		for(int i = 0; i <25 ; i++){
			for(int j = 0; j < 25; j++){
				double x = 25*Math.random();
				double y = 25*Math.random();
				//add(new SurfacePoint(x, y, 25*((x-5)*(x-7)*(x-10)+30)/35165));
				add(new SurfacePoint(x, y, x+.0005*Math.random()));
				//add(new SurfacePoint());
			}
		}
	}
	
	public Surface (SurfacePoint[] surfaceAsArray, int unitSize, int clusterSize){
		this.unitSize = unitSize;
		for(int i = 0; i < surfaceAsArray.length; i++){
			add(surfaceAsArray[i]);
		}
	}
	
	public void addPoint(SurfacePoint pointToAdd){
		add(pointToAdd);
	}
	
	public void createClusterPlanes(){
		allocateSurfacePointsToSurfaceGrid();
		setAllNormalVector();
		surfaceGrid.buildPlanes();
		surfaceGrid.generateVisualGrid(250, 250);
		//newPrintStuff();
		//printStuff();
	}

	private void setAllNormalVector() {
		for(int i = 0; i < size(); i++){
			get(i).setNormalVector();
		}
	}


	public void allocateSurfacePointsToSurfaceGrid() {
		surfaceGrid = new SurfaceGrid(this);
	}
	
	public void printStuff(){
		BufferedImage buf = new BufferedImage(surfaceGrid.size(), surfaceGrid.size(), BufferedImage.TYPE_INT_RGB);
		Raster r = buf.getData();
		//Vector<Line2D.Double> lines = new Vector<Line2D.Double>();
		WritableRaster wR = r.createCompatibleWritableRaster();
		double maxSoFar = 0;
		double minSoFar = Double.MAX_VALUE;
		for(int i = 0; i < surfaceGrid.size(); i++){
			for(int j = 0; j < surfaceGrid.size(); j++){
				if(surfaceGrid.getGridSection(i, j).getDistanceToNearestSurfacePoint() == Integer.MAX_VALUE){
					System.out.println("Oh dear");
				}
//				if(surfaceGrid.getGridSection(i, j).containsSurfacePoint()){
//					//System.out.println("Oh No");
//					Vector<SurfacePoint> temp = surfaceGrid.getGridSection(i, j).getSurfacePoint().getNearbyPoints();
//					for(int k = 0; k < 1;k++){
//						new Line2D.Double(i, j, temp.get(k).xLocationOnGrid, temp.get(k).yLocationOnGrid);
//						lines.add(new Line2D.Double(i, j, temp.get(k).xLocationOnGrid, temp.get(k).yLocationOnGrid));
//					}
//				}
				for(int k = 0; k < wR.getNumBands(); k++){
					if (surfaceGrid.getGridSection(i, j).getZValue()> maxSoFar){
						maxSoFar = surfaceGrid.getGridSection(i, j).getZValue();
					}
					if (surfaceGrid.getGridSection(i, j).getZValue()< minSoFar){
						minSoFar = surfaceGrid.getGridSection(i, j).getZValue();
					}
					wR.setSample(i, j, k, surfaceGrid.getGridSection(i, j).getZValue());
				}
				//System.out.print(surfaceGrid.getGridSection(i, j).getDistanceToNearestSurfacePoint());
			}
			//System.out.println();
		}
		for(int i = 0; i < wR.getWidth(); i++){
			for(int j = 0; j < wR.getHeight(); j++){
				if((surfaceGrid.getGridSection(i, j).getZValue()-minSoFar)/(maxSoFar-minSoFar)> 1 ||(surfaceGrid.getGridSection(i, j).getZValue()-minSoFar)/(maxSoFar-minSoFar)<0){
					System.out.println(Math.round(surfaceGrid.getGridSection(i, j).getZValue())+" ");
				}
				for(int k = 0; k < wR.getNumBands(); k++){
					wR.setSample(i, j, k, (int)Math.round(255*(surfaceGrid.getGridSection(i, j).getZValue()-minSoFar)/(maxSoFar-minSoFar)));
				}
			}
			//System.out.println();
		}
		Vector v = new Vector();
		for(int i = 0; i < pointsHit.size(); i++){
			Vector lines = new Vector();
			for(int j = 0; j < pointsHit.get(i).size()-1; j++){
				lines.add(new Line2D.Double(pointsHit.get(i).get(j)[0], pointsHit.get(i).get(j)[1], pointsHit.get(i).get(j+1)[0], pointsHit.get(i).get(j+1)[1]));
			}
			v.add(lines);
		}
		//System.out.println(maxSoFar);
		buf.setData(wR);
		ImageDisplay imDisp= new ImageDisplay(buf, v);
		JFrame f = new JFrame("myImage");
		JScrollPane imScroll = new JScrollPane(imDisp);
		f.add(imScroll);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}
	
	public void newPrintStuff(){
		BufferedImage buf = new BufferedImage(surfaceGrid.getDisplayXSize(), surfaceGrid.getDisplayYSize(), BufferedImage.TYPE_INT_RGB);
		Raster r = buf.getData();
		Vector<Line2D.Double> lines = new Vector<Line2D.Double>();
		WritableRaster wR = r.createCompatibleWritableRaster();
		double maxSoFar = 0;
		double minSoFar = Double.MAX_VALUE;
		for(int i = 0; i < surfaceGrid.getDisplayXSize(); i++){
			for(int j = 0; j < surfaceGrid.getDisplayYSize(); j++){
				for(int k = 0; k < wR.getNumBands(); k++){
					if (surfaceGrid.getVisualGridPoint(i, j).getZValue()> maxSoFar){
						maxSoFar = surfaceGrid.getVisualGridPoint(i, j).getZValue();
					}
					if (surfaceGrid.getVisualGridPoint(i, j).getZValue()< minSoFar){
						minSoFar = surfaceGrid.getVisualGridPoint(i, j).getZValue();
					}
					wR.setSample(i, j, k, surfaceGrid.getVisualGridPoint(i, j).getZValue());
				}
				//System.out.print(surfaceGrid.getVisualGridPoint(i, j).getDistanceToNearestSurfacePoint());
			}
			//System.out.println();
		}
		for(int i = 0; i < wR.getWidth(); i++){
			for(int j = 0; j < wR.getHeight(); j++){
				if((surfaceGrid.getVisualGridPoint(i, j).getZValue()-minSoFar)/(maxSoFar-minSoFar)> 1 ||(surfaceGrid.getVisualGridPoint(i, j).getZValue()-minSoFar)/(maxSoFar-minSoFar)<0){
					System.out.println(Math.round(surfaceGrid.getVisualGridPoint(i, j).getZValue())+" ");
				}
				for(int k = 0; k < wR.getNumBands(); k++){
					wR.setSample(i, j, k, (int)Math.round(255*(surfaceGrid.getVisualGridPoint(i, j).getZValue()-minSoFar)/(maxSoFar-minSoFar)));
				}
			}
			//System.out.println();
		}
		//System.out.println(maxSoFar);
		buf.setData(wR);
		Vector v = new Vector();
		v.add(lines);
		ImageDisplay imDisp= new ImageDisplay(buf, v);
		JFrame f = new JFrame("myImage");
		JScrollPane imScroll = new JScrollPane(imDisp);
		f.add(imScroll);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	}

	public double[] putt(double xStart, double yStart, double xVel, double yVel, double cupX, double cupY, Vector<ConstantForce> constantForces, double ballMass) {
		// TODO Auto-generated method stub
		surfaceGrid.setCupLocation(cupX, cupY);
		System.out.println("in surface");
		for(int i = 0; i < size(); i++){
			get(i).createPotential(constantForces);
			get(i).inputBallMass(ballMass);
		}
		boolean onGreen = true;
		Vector<Double[]> distAndVel = new Vector<Double[]>();
		pointsHit = new Vector<Vector<Integer[]>>();
		
		double adjustment = 1.01;
		Matrix cupPos = new Matrix(new double[][] {{cupX-xStart}, {cupY-yStart}, {0} });
		int badCount = 0;
		double testVel = 1;
		while (onGreen ){
			double distanceToCup = Math.sqrt(Math.pow(cupX-xStart, 2)+Math.pow(cupY-yStart, 2));
			double xStartVel = testVel*Math.random();
			double yStartVel = Math.sqrt(testVel*testVel-xStartVel*xStartVel);
			Vector<Integer[]> grids = new Vector<Integer[]>();
			double x = xStart;
			double y = yStart;
			xVel = xStartVel;
			yVel = yStartVel;
			boolean putting = true;
			double[] returned = new double[3];
			while(putting){
				returned = surfaceGrid.putt(x, y, xVel, yVel);
				DecimalFormat df = new DecimalFormat("0.00");
				//System.out.println(df.format(returned[0])+" "+df.format(returned[1])+" "+df.format(returned[2])+" "+df.format(returned[3])+" "+df.format(returned[4])+" "+df.format(returned[5])+" "+ df.format(returned[6])+" ");
				if(returned[0]>0){
					xVel=returned[3];
					yVel=returned[4];
					x=returned[1];
					y=returned[2];
					grids.add(new Integer[] {(int)returned[5], (int)returned[6]});
					
					if(Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2))>distanceToCup){
						if(Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2))>distanceToCup+.01){
							putting = false;
						}
					}else{
						distanceToCup = Math.sqrt(Math.pow(cupX-x, 2)+Math.pow(cupY-y, 2));
					}
				}else{
					putting = false;
				}
			}
			distAndVel.add(new Double[]{distanceToCup, xStartVel, yStartVel});
			pointsHit.add(grids);
//			if(returned.get(0, 0)>1){
//				Matrix ballPos = new Matrix(new double[][] {{ 0, 0, returned.get(2, 0)-yStart}, { 0, 0, -returned.get(1, 0)+xStart}, { -returned.get(2, 0)+yStart, 0, returned.get(1, 0)-xStart}});
//				Matrix crossProduct = ballPos.times(cupPos);
//				crossProduct.print(2, 2);
//				if(returned.get(2, 0)>yStart&&returned.get(2, 0)<cupY&&returned.get(1, 0)>xStart&&returned.get(1, 0)<cupX){
//					yVel = yVel*adjustment;
//					xVel = xVel*adjustment;
//				}else
//				if(crossProduct.get(2, 0) > 0){
//					xVel*=adjustment;
//					yVel/= adjustment;
//				}else
//				if(crossProduct.get(2, 0) < 0){
//					yVel*=adjustment;
//					xVel/= adjustment;
//				}
//				
//				
//			}
//			else if(returned.get(0, 0)==-2){
//				Matrix ballPos = new Matrix(new double[][] {{ 0, 0, returned.get(2, 0)-yStart}, { 0, 0, -returned.get(1, 0)+xStart}, { -returned.get(2, 0)+yStart, 0, returned.get(1, 0)-xStart}});
//				Matrix crossProduct = ballPos.times(cupPos);
//				if(crossProduct.get(2, 0) > 0){
//					xVel+=adjustment;
//					yVel-= adjustment;
//				}
//				if(crossProduct.get(2, 0) < 0){
//					yVel+=adjustment;
//					xVel-= adjustment;
//				}
//			}
//			else if(returned.get(0, 0)==-1){
//				xVel = xVel*2;
//				yVel = yVel*2;
//				onGreen = false;
//			}else{
//				onGreen = false;
//			}
//			adjustment-=.01;
//			returned.print(2, 2);
			if(distanceToCup < 1){
				onGreen = false;
			}
			badCount++;
			if (badCount > 100){
				if(testVel>20){
					onGreen = false;
				}
				badCount = 0;
				testVel++;
			}
		}
		//surfaceGrid.setPointsHit(pointsHit);
		double closest = Double.MAX_VALUE;
		double[] returnVel = new double[2];
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i = 0; i < distAndVel.size(); i++){
			if(distAndVel.get(i)[0]<closest){
				returnVel[0] = distAndVel.get(i)[1];
				returnVel[1] = distAndVel.get(i)[2];
			}
			System.out.println(distAndVel.get(i)[0]+ " "+df.format(distAndVel.get(i)[1])+" "+df.format(distAndVel.get(i)[2]));
		}
		printStuff();
		newPrintStuff();
		return returnVel;
		
	}

	
	
}
