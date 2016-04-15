package de.carlos.machine;

import java.util.ArrayList;

public class DataPoint<IN, OUT> {
	
	public DataPoint(IN arrayList, OUT i) {
		this.values = arrayList;
		this.result = i;
	}
	public IN values;
	public OUT result; 

}
