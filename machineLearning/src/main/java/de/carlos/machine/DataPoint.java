package de.carlos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class DataPoint {
	
	public DataPoint(List<Double> arrayList, Double i) {
		this.values = arrayList;
		this.result = i;
	}
	public List<Double> values;
	public Double result; 

	@Override
	public String toString(){
		return this.values.toString()+"->"+this.result;
	}

	public static List<DataPoint> buildForFunction(Heuristic h){
		return buildForFunction(h::apply, h.getParameters().size()-1);
	}
	
	public static List<DataPoint> buildForFunction(Function<List<Double>, Double> f, int valueCount){
		
		Random r = new Random(0);
		
		List<DataPoint> data = new ArrayList<>();
		for (int i = 0; i<100; i++){
			
			List<Double> values = new ArrayList<Double>();
			for(int v = 0; v<valueCount;v++){
				values.add(r.nextDouble());
			}
			
			data.add(new DataPoint(values, f.apply(values)));
		}
		
		return data;
	}
}
