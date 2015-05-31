package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;

public class FoodOptimize {

	public Map<Food, Double> optimize(List<Food> foods) {
		
		LinearObjectiveFunction objective = new LinearObjectiveFunction(filterValid(foods)
				.mapToDouble(f -> f.price).toArray(), 0);		
		
		

		class ConstraintArray extends ArrayList<LinearConstraint>{
			
			public void addC(ToDoubleFunction<Food> f, Relationship r, double val){
				this.add(new LinearConstraint(filterValid(foods).mapToDouble(f)
						.toArray(), r, val));
			}
		};
		ConstraintArray constraints = new ConstraintArray();
		
		
		//Basic Elements
		constraints.addC(f -> f.kohlenhydrate
				, Relationship.EQ, 340.0);
		constraints.addC(f -> f.fett
				, Relationship.EQ, 80.0);
		constraints.addC(f -> f.protein
				, Relationship.EQ, 60.0);
		constraints.addC(f -> f.ballast
				, Relationship.EQ, 25.0);
		
		//Spurenelemente
		constraints.addC(f -> f.calcium
				, Relationship.GEQ, 1.0);			
		constraints.addC(f -> f.eisen
						, Relationship.GEQ, 10.0E-3);		
		constraints.addC(f -> f.iod		
				, Relationship.GEQ, 200.0E-6);					
		constraints.addC(f -> f.fluorid
				, Relationship.GEQ, 3.8E-3);				
		constraints.addC(f -> f.magnesium
				, Relationship.GEQ, 350.0E-3);	
		constraints.addC(f -> f.zink
				, Relationship.GEQ, 10.0E-3);	
		
		//Vitamine
		constraints.addC(f -> f.vitaminA
				, Relationship.GEQ, 1.0E-6);	
		constraints.addC(f -> f.vitaminB1
				, Relationship.GEQ, 1.2E-3);			
		constraints.addC(f -> f.vitaminB2
				, Relationship.GEQ, 1.4E-3);		
		constraints.addC(f -> f.vitaminB6
				, Relationship.GEQ, 1.5E-3);		
		constraints.addC(f -> f.vitaminB12
				, Relationship.GEQ, 3E-6);				
		constraints.addC(f -> f.vitaminC
				, Relationship.GEQ, 100E-3);		
		constraints.addC(f -> f.vitaminD
				, Relationship.GEQ, 5E-6);				
		
		
		constraints.addC(f -> f.vitaminE
				, Relationship.GEQ, 14E-3);				
		constraints.addC(f -> f.vitaminE
				, Relationship.LEQ, 300E-3);			
		
		
		constraints.addC(f -> f.niacin
				, Relationship.GEQ, 16E-3);	
		
		constraints.addC(f -> f.folat
				, Relationship.GEQ, 400E-6);			
		
		
		//Unavailable
//		constraints.addC(f -> f.selen
//				, Relationship.GEQ, 40.0E-6);			 
		

		PointValuePair pvp = new SimplexSolver(10E-12,100)
		.optimize(objective, new LinearConstraintSet(constraints),new NonNegativeConstraint(true));
			
		Map<Food, Double> result = new HashMap<>();
		for (int i = 0;i<pvp.getKey().length;i++){
			double factor = pvp.getKey()[i];
			if (factor > 0){
				result.put((Food) filterValid(foods).toArray()[i], factor);
			}
		}
		
		return result;
	}

	private Stream<Food> filterValid(List<Food> foods) {
		Stream<Food> valid = foods.stream().filter(p -> p.price != null);
		return valid;
	}

	private double[] fromListD(List<Double> l) {
		double[] a = new double[l.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = l.get(i);
		}
		return a;
	}
	
}
