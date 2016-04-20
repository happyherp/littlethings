package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureScaling {
	
	
	List<Double> meanNormalizers = new ArrayList<>();
	
	List<Double> scalers = new ArrayList<>();
	
	
	public FeatureScaling(List<DoubleDataPoint> data) {
		for (int i = 0;i <data.get(0).values.size();i++){
			final int index = i;
			List<Double> ithData = data.stream()
				.map(p->p.values.get(index))
				.collect(Collectors.toList());
			meanNormalizers.add(calculateMean(ithData));
			scalers.add(calculateScale(ithData));
		}
		
	}
	
	
	private Double calculateMean(List<Double> ithData) {
		return ithData.get(ithData.size()/2);
	}


	private Double calculateScale(List<Double> ithData) {
		Double max = ithData.stream().max((a,b)->a.compareTo(b)).get(); 
		Double min = ithData.stream().min((a,b)->a.compareTo(b)).get(); 
		return max-min ;
	}


	public List<Double> convertToScale(List<Double> input){
		if (input.size() != meanNormalizers.size()){
			throw new RuntimeException("Dimensions did not match.");
		}
		
		ArrayList<Double> output = new ArrayList<>();
		for (int i = 0;i<input.size();i++){
			double scaled = (input.get(i)-meanNormalizers.get(i))/scalers.get(i);	
			output.add(scaled);
		}
		
		return output;
	}
	

	public List<DoubleDataPoint> convertDataPointsToScale(List<DoubleDataPoint> input) {
		return input.stream()
				.map(dp->new DoubleDataPoint(this.convertToScale(dp.values), dp.result))
				.collect(Collectors.toList());
	}

	
	public static List<DoubleDataPoint> scaleIt(List<DoubleDataPoint> input){
		FeatureScaling scale = new FeatureScaling(input);
		return scale.convertDataPointsToScale(input);
	}



}
