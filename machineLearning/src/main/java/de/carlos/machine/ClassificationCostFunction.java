package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;

public class ClassificationCostFunction implements CostFunction {

	@Override
	public double calculateCost(Heuristic h,  List<DataPoint> points ){
		
		return -1 * 
				points.stream()
					.mapToDouble(db -> 
						db.result * Math.log(h.apply(db.values)) 
						+ (1.0d-db.result) * Math.log(1-h.apply(db.values)) )
					.sum()
				/ points.size();		
	}

	@Override
	public List<Double> calculateNewParameters(Heuristic h, List<DataPoint> data, double learningRate) {
		List<Double> newParameters = new ArrayList<Double>();
		for (int j = 0; j<h.getParameters().size();j++){
			double sum = 0D;
			for (DataPoint dp: data){
				double x = j==0?1D:dp.values.get(j-1);
				sum += (h.apply(dp.values) - dp.result) * x;
			}
			newParameters.add(h.getParameters().get(j) - sum *learningRate );
		}
		return newParameters;
	}

}
