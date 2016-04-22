package de.carlos.machine;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Heuristic implements Cloneable{

	/**
	 * First parameters p0 to pN to be applied like p0+p1*x1+p2*x2 ... pN * xN.
	 * 
	 */
	protected final List<Double> parameters;

	public Heuristic(List<Double> parameters) {
		this.parameters = Collections.unmodifiableList(parameters);
	}

	public List<Double> getParameters() {
		return parameters;
	}
	
	public Heuristic improve(List<DataPoint> data, double learningRate){
		return this.withNewParameters(this.getCostFunction().calculateNewParameters(this, data, learningRate));
	}
	
	public abstract Heuristic withNewParameters(List<Double> newParameters);


	public Heuristic asZeroParmater() {
		return this.withNewParameters(this.getParameters().stream()
				.map(p->0.0D)
				.collect(Collectors.toList()));

	}

	public abstract Double apply(List<Double> in);
	
	public abstract CostFunction getCostFunction();

	public Double calculateCost(List<DataPoint> data) {
		return this.getCostFunction().calculateCost(this, data);
	}
	

}
