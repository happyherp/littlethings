package de.carlos.machine;

import java.util.Random;

public class NNNode {
	
	private double[] parameters;

	public NNNode(int i) {
		setParameters(new double[i+1]);
	}

	public double[] getParameters() {
		return parameters;
	}

	public void setParameters(double[] parameters) {
		this.parameters = parameters;
	}
	
	public double calculate(double[] values){
		if (values.length != parameters.length-1){
			throw new RuntimeException("Sized of values and parameters did not match. ");
		}
		

		Double result = parameters[0];
		for (int i = 0; i<values.length;i++){
			result += values[i] * parameters[i+1];
		}		
		
		return result;
	}

}
