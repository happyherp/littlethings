package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class  GradientDescent {
	
	protected final LinearHeuristic h;
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
			LinearHeuristic h, 
			List<DataPoint> data,
			double learningRate){
		
		this.h = h;
		this.data = data;
		this.learningRate = learningRate;
	}
	
	public void doIteration(){
		
		List<Double> newParameters = calculateNewParameters();		
		this.h.setParameters(newParameters);
	}
	
	public int converge(){
		
		double limit = 10.0e-6;
		
		//System.out.println(descent.getCost() + "  "+descent.getH().getParameters());
		double prevCost = this.getCost();
		int i;
		boolean converged = false; 
		for (i = 0; i<2000 && !converged;i++){
			this.doIteration();
			System.out.println(this.getCost() + "  "+this.getH().getParameters());
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

	public List<Double> calculateNewParameters() {
		List<Double> newParameters = new ArrayList<Double>();
		for (int j = 0; j<h.getParameters().size();j++){
			double sum = 0D;
			for (DataPoint dp: data){
				double x = j==0?1D:dp.values.get(j-1);
				sum += (h.apply(dp.values) - dp.result) * x;
			}
			sum = sum*this.learningRate / h.getParameters().size();
			newParameters.add(h.getParameters().get(j) - sum);
		}
		return newParameters;
	}
	
	public Double getCost(){
		return new CostFunction().calculateCost(this.h, this.data);
	}

	public LinearHeuristic getH() {
		return h;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public List<DataPoint> getData() {
		return data;
	}

	
}
