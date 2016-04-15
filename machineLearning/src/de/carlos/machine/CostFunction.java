package de.carlos.machine;

import java.util.List;

public class CostFunction<IN, OUT> {

	private DistanceFunction<OUT> dist;


	public CostFunction(DistanceFunction<OUT> dist){
		this.dist = dist;
	}
	
	
	public double calculateCost(Heuristic<IN, OUT> h,  List<? extends DataPoint<IN, OUT>> points ){
		
		return points.stream()
			.mapToDouble(db -> Math.pow( dist.calculateDistance(db.result, h.apply(db.values)) , 2d)).sum()
			/ (2d*points.size());
		
	}
	
	
	public static CostFunction<List<Double>, Double> doubleCostFunction(){
		return new CostFunction<List<Double>, Double>( (a,b) -> a-b );
	}
	
}
