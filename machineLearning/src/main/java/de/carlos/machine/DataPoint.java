package de.carlos.machine;

import java.util.List;

public class DataPoint {
	
	public DataPoint(List<Double> arrayList, Double i) {
		this.values = arrayList;
		this.result = i;
	}
	public List<Double> values;
	public Double result; 

}
