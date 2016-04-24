package de.carlos.machine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class  GradientDescent {
	
	protected Heuristic h;
	protected double learningRate;
	protected List<DataPoint> data;
	
	public GradientDescent(List<DataPoint> data,
			double learningRate) {
		
		this.data = data;
		
		List<Double> parameters =DoubleStream.generate(()->0.0D)
				.limit(data.get(0).values.size()+1)
				.mapToObj(Double::new)
				.collect(Collectors.toList());
		this.h = new LinearHeuristic(parameters );		
		this.learningRate = learningRate;
	}

	public GradientDescent(
			Heuristic h, 
			List<DataPoint> data,
			double learningRate){
		
		this.h = h;
		this.data = data;
		this.learningRate = learningRate;
	}
	
	public void doIteration(){
		
		List<Double> newParameters = this.h.getCostFunction()
				.calculateNewParameters(this.h, this.getData(),this.learningRate);		
		this.h = h.withNewParameters(newParameters);
		System.out.println(this.getCost() + "  "+this.getH().getParameters());
		
	}
	
	public int converge(){
		
		double limit = 10.0e-6;
		
		System.out.println(this.getCost() + "  "+this.getH().getParameters());
		double prevCost = this.getCost();
		int i;
		boolean converged = false; 
		for (i = 0; i<2000 && !converged;i++){
			this.doIteration();
			if (!(prevCost >= this.getCost())){
				throw new NoDescentException("The algorithm did not descend after an iteration");
			}
			converged = prevCost - this.getCost() < limit;
			prevCost = this.getCost();
		}
		if (!converged){
			throw new NoConvergionException("The algorithm did not converge before hitting the limit. ");
		}
		
		return i;
	}


	
	public Double getCost(){
		return h.getCostFunction().calculateCost(this.h, this.data);
	}

	public Heuristic getH() {
		return h;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public List<DataPoint> getData() {
		return data;
	}

	
}
