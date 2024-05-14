package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;

public class SquaredError implements CostFunction{


	
	@Override
	public double calculateCost(Heuristic h,  List<DataPoint> points ){
		
		return points.stream()
			.mapToDouble(db -> Math.pow(db.result - h.apply(db.values) , 2d)).sum()
			/ (2d*points.size());
		
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
			sum = sum*learningRate / h.getParameters().size();
			newParameters.add(h.getParameters().get(j) - sum);
		}
		return newParameters;
	}
	
	
}
