package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LinearHeuristic {

	/**
	 * First parameters p0 to pN to be applied like p0+p1*x1+p2*x2 ... pN * xN.
	 * 
	 */
	private List<Double> parameters;

	public LinearHeuristic(List<Double> parameters) {
		this.parameters = parameters;
	}

	public List<Double> getParameters() {
		return parameters;
	}

	public void setParameters(List<Double> parameters) {
		this.parameters = parameters;
	}

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

	public LinearHeuristic asZeroParmater() {
		LinearHeuristic copy = new LinearHeuristic(
				this.getParameters().stream()
				.map(p->0.0D)
				.collect(Collectors.toList()));
		
		return copy;
	}

}
