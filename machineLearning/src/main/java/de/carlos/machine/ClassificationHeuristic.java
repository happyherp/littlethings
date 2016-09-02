package de.carlos.machine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class ClassificationHeuristic extends Heuristic {

	public ClassificationHeuristic(List<Double> parameters2) {
		super(parameters2);
	}

	public ClassificationHeuristic(int inputValueCount) {
		super(DoubleStream.generate(()->0.0d)
				.limit(inputValueCount+1)
				.mapToObj(a->a)
				.collect(Collectors.toList()));
	}

	@Override
	public Double apply(List<Double> in) {
		
		if (parameters.size()-1 != in.size()){
			throw new RuntimeException("Parameter did not match input. "+ parameters.size()+" -1 != "+in.size());
		}		
		
		Double result = parameters.get(0);
		for (int i = 0; i<in.size();i++){
			result += in.get(i) * parameters.get(i+1);
		}		

		return  1.0D / (1.0D+ Math.pow(Math.E, -result));
	}

	@Override
	public CostFunction getCostFunction() {
		return new ClassificationCostFunction();
	}

	@Override
	public ClassificationHeuristic withNewParameters(List<Double> newParameters) {
		if (newParameters.size() != this.parameters.size()){
			throw new RuntimeException();
		}		
		return new ClassificationHeuristic(newParameters);
	}

}
