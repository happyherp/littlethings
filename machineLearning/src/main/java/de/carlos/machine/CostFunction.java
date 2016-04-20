package de.carlos.machine;

import java.util.List;

public class CostFunction{


	
	public double calculateCost(LinearHeuristic h,  List<DataPoint> points ){
		
		return points.stream()
			.mapToDouble(db -> Math.pow(db.result - h.apply(db.values) , 2d)).sum()
			/ (2d*points.size());
		
	}
	
	
	
}
