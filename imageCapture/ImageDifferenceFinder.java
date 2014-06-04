package imageCapture;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Vector;

public class ImageDifferenceFinder {
	
	
	public static Vector<BufferedImage> findDifferences(Vector<BufferedImage> imgVect){
		Vector<BufferedImage> difImg = new Vector<BufferedImage>();
		for(int i = 0; i < imgVect.size()-1; i++){
			BufferedImage bufA = imgVect.get(i);
			BufferedImage bufB = imgVect.get(i+1);
			Raster rA = bufA.getData();
			Raster rB = bufB.getData();
			WritableRaster wR = rA.createCompatibleWritableRaster();
			int numBands = rA.getNumBands();
			for(int j = 0; j < numBands; j++){
				for(int k = rA.getMinX(); k<rA.getWidth(); k++){
					for(int l = rA.getMinY(); l<rA.getHeight(); l++){
						wR.setSample(k, l, j, Math.abs(rA.getSample(k, l, j)-rB.getSample(k, l, j)));
					}
				}
			}
			
			bufA.setData(wR);
			difImg.add(bufA);
		}
		int[][] arrR = new int[difImg.get(0).getWidth()][difImg.get(0).getHeight()];
		int[][] arrG = new int[difImg.get(0).getWidth()][difImg.get(0).getHeight()];
		int[][] arrB = new int[difImg.get(0).getWidth()][difImg.get(0).getHeight()];
		for(int i = 0; i < difImg.size(); i++){
			BufferedImage bufA = difImg.get(i);
			Raster rA = bufA.getData();
			int numBands = rA.getNumBands();
			for(int j = 0; j < numBands; j++){
				for(int k = rA.getMinX(); k<rA.getMinX() +rA.getWidth(); k++){
					for(int l = rA.getMinY(); l<rA.getMinY()+rA.getHeight(); l++){
						if(j==0 &&rA.getSample(k, l, j)>25){
							arrR[k-rA.getMinX()][l-rA.getMinY()] ++;
						}
						if(j==1 &&rA.getSample(k, l, j)>25){
							arrG[k-rA.getMinX()][l-rA.getMinY()] ++;
						}
						if(j==2 &&rA.getSample(k, l, j)>25){
							arrB[k-rA.getMinX()][l-rA.getMinY()] ++;
						}
					}
				}
			}
		}
		BufferedImage bufR = difImg.get(0);
		Raster rR = bufR.getData();
		WritableRaster wrR = rR.createCompatibleWritableRaster();
		BufferedImage bufG = difImg.get(1);
		Raster rG = bufG.getData();
		WritableRaster wrG = rG.createCompatibleWritableRaster();
		BufferedImage bufB = difImg.get(2);
		Raster rB = bufB.getData();
		WritableRaster wrB = rB.createCompatibleWritableRaster();
		for(int j = 0; j < rR.getNumBands(); j++){
			for(int k = rR.getMinX(); k<rR.getMinX()+rR.getWidth(); k++){
				for(int l = rR.getMinY(); l<rR.getMinY()+rR.getHeight(); l++){
					if(j==0){
						wrR.setSample(k, l, 0, 255);
						wrR.setSample(k, l, 1, 255);
						wrR.setSample(k, l, 2, 255);
						if(arrR[k-rR.getMinX()][l-rR.getMinY()]>imgVect.size()/2){
							wrR.setSample(k, l, 1, 0);
							wrR.setSample(k, l, 2, 0);
							//System.out.println(k + " " + l + " " + j + " " );
						}
					}
					if(j==1){
						wrG.setSample(k, l, 0, 255);
						wrG.setSample(k, l, 1, 255);
						wrG.setSample(k, l, 2, 255);
						if(arrG[k-rR.getMinX()][l-rR.getMinY()]>imgVect.size()/2){
							wrG.setSample(k, l, 0, 0);
							wrG.setSample(k, l, 2, 0);
							//System.out.println(k + " " + l + " " + j + " " );
						}
					}
					if(j==2){
						wrB.setSample(k, l, 0, 255);
						wrB.setSample(k, l, 1, 255);
						wrB.setSample(k, l, 2, 255);
						if(arrB[k-rR.getMinX()][l-rR.getMinY()]>imgVect.size()/5){
							wrB.setSample(k, l, 0, 0);
							wrB.setSample(k, l, 1, 0);
							//System.out.println(k + " " + l + " " + j + " " );
						}
					}
				}
			}
		}
		difImg.removeAllElements();
		bufR.setData(wrR);
		bufG.setData(wrG);
		bufB.setData(wrB);
		difImg.add(bufR);
		difImg.add(bufG);
		difImg.add(bufB);
		return difImg;
		
		
	}
}
