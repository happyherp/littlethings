package de.carlos.machine;

import java.util.List;

public class AlphaUpdatingGD extends GradientDescent {

	public AlphaUpdatingGD(List<DataPoint> data, double startlearningRate) {
		super(data, startlearningRate);
	}
	
	public AlphaUpdatingGD(List<DataPoint> data) {
		super(data, 100.0d);
	}

	@Override
	public void doIteration(){
		
		
		Double oldCost = this.getCost();				
		Heuristic tmpH = this.getH().improve(getData(), getLearningRate());		
		Double newCost = tmpH.calculateCost(getData());
		
		for (int i = 0;i<100 && newCost>oldCost ;i++){
			this.learningRate *= 0.3;
			System.out.println("Updated learning rate to " + this.learningRate);
			tmpH = this.getH().improve(getData(), getLearningRate());		
			newCost = tmpH.calculateCost(getData());
		}		
		
		this.h = tmpH;
	}

}
