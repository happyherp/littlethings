package de.carlos.machine;


public class DataPoint<IN, OUT> {
	
	public DataPoint(IN arrayList, OUT i) {
		this.values = arrayList;
		this.result = i;
	}
	public IN values;
	public OUT result; 

}
