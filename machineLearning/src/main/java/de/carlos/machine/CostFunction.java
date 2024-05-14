package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;

public interface CostFunction {

	double calculateCost(Heuristic h, List<DataPoint> points);

	List<Double> calculateNewParameters(Heuristic h, List<DataPoint> data, double learningRate);
	
}
