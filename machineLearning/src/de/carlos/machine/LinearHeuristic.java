package de.carlos.machine;

import java.util.List;

public class LinearHeuristic implements Heuristic<List<Double>, Double> {

	/**
	 * First parameters p0 to pN to be applied like p0+p1*x1+p2*x2 ... pN * xN.
	 * 
	 */
	private List<Double> parameters;

	public LinearHeuristic(List<Double> parameters) {
		this.parameters = parameters;
	}

	@Override
	public List<Double> getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(List<Double> parameters) {
		this.parameters = parameters;
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

}
