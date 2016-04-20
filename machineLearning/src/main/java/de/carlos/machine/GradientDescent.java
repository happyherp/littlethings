package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class  GradientDescent {
	
	private final LinearHeuristic h;
	private final  double learningRate;
	private List<? extends DataPoint<List<Double>, Double>> data;

	public GradientDescent(
			LinearHeuristic h, 
			List<? extends DataPoint<List<Double>, Double>> data,
			double learningRate){
		
		this.h = h;
		this.data = data;
		this.learningRate = learningRate;
	}
	
	public void doIteration(){
		
		List<Double> newParameters = new ArrayList<Double>();
		for (int j = 0; j<h.getParameters().size();j++){
			double sum = 0D;
			for (DataPoint<List<Double>, Double> dp: data){
				double x = j==0?1D:dp.values.get(j-1);
				sum += (h.apply(dp.values) - dp.result) * x;
			}
			sum = sum*this.learningRate / h.getParameters().size();
			newParameters.add(h.getParameters().get(j) - sum);
		}
		
		this.h.setParameters(newParameters);
	}
	
	public Double getCost(){
		return CostFunction.doubleCostFunction().calculateCost(this.h, this.data);
	}

	public LinearHeuristic getH() {
		return h;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public List<? extends DataPoint<List<Double>, Double>> getData() {
		return data;
	}

	
}
