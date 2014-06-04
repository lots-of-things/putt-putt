package puttingGreen;

import java.util.Vector;

public class VisualGridPoint extends Vector<SurfaceGridSection>{

	public double getZValue() {
		double zVal = 0;
		for(int i = 0 ; i< size(); i++){
			zVal += get(i).zValue;
		}
		if(size()>0){
			return zVal/size();
		}
		else{
			return 0;
		}
	}

}
