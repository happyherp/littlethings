package de.carlos.machine;

import java.util.List;

public class AlphaUpdatingGD extends GradientDescent {

	private boolean noGoodLearningRateFound = false;

	public AlphaUpdatingGD(List<DataPoint> data, double startlearningRate) {
		super(data, startlearningRate);
	}
	
	public AlphaUpdatingGD(List<DataPoint> data) {
		super(data, 1.0d);
	}

	public AlphaUpdatingGD(Heuristic h, List<DataPoint> data, double learningRate) {
		super(h,data,learningRate);
	}

	@Override
	public void doIteration(){
		
		if (this.noGoodLearningRateFound){
			return;
		}
		
		Double oldCost = this.getCost();				
		Heuristic tmpH = this.getH().improve(getData(), getLearningRate());		
		Double newCost = tmpH.calculateCost(getData());
		
		for (int i = 0;i<100 && newCost>oldCost ;i++){
			this.learningRate *= 0.3;
			System.out.println("Updated learning rate to " + this.learningRate);
			tmpH = this.getH().improve(getData(), getLearningRate());		
			newCost = tmpH.calculateCost(getData());
		}	
		
		if(false && !(newCost>oldCost)){
			this.noGoodLearningRateFound  = true;
			System.out.println("noGoodLearningRateFound");
		}else{			
			this.h = tmpH;		
			System.out.println(this.getCost() + "  "+this.getH().getParameters());
			this.learningRate *= 1.1;
		}
	}

}
