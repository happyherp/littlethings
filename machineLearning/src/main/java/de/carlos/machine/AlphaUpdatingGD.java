package de.carlos.machine;

import java.util.List;

public class AlphaUpdatingGD extends GradientDescent {

	public AlphaUpdatingGD(List<? extends DataPoint<List<Double>, Double>> data, double startlearningRate) {
		super(data, startlearningRate);
	}
	
	@Override
	public void doIteration(){
		
		
		Double oldCost = this.getCost();				
		LinearHeuristic tmpH = new LinearHeuristic(calculateNewParameters());				
		Double newCost = CostFunction.doubleCostFunction().calculateCost(tmpH, this.getData());
		
		for (int i = 0;i<100 && newCost>oldCost ;i++){
			this.learningRate *= 0.3;
			System.out.println("Updated learning rate to " + this.learningRate);
			tmpH = new LinearHeuristic(calculateNewParameters());
			newCost = CostFunction.doubleCostFunction().calculateCost(tmpH, this.getData());
		}		
		
		this.h.setParameters(tmpH.getParameters());
	}

}
