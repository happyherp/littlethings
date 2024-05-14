package de.carlos.machine;

import java.util.List;

public class LinearHeuristic extends Heuristic {

	public LinearHeuristic(List<Double> parameters) {
		super(parameters);
	}

	@Override
	public Double apply(List<Double> in) {
		
		if (parameters.size()-1 != in.size()){
			throw new RuntimeException("Parameter did not match input. "+ parameters.size()+" -1 != "+in.size());
		}
		
		Double result = 0.0;
		for (int i = 0; i<in.size();i++){
			result += in.get(i) * parameters.get(i+1);
		}		
		
		result += parameters.get(0);
		return result;
	}

	@Override
	public CostFunction getCostFunction() {
		return new SquaredError();
	}
	
	@Override
	public LinearHeuristic withNewParameters(List<Double> newParameters) {
		if (newParameters.size() != this.parameters.size()){
			throw new RuntimeException();
		}		
		return new LinearHeuristic(newParameters);
	}	

}
