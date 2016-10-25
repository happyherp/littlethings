package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexFood.swissDB.SwissDB;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class CostByNutrient {
	
	private static final double FACTOR = 1.1;
	
	public static void main(String[] args){
		
		
		NutritionTarget target = NutritionTarget.anyCalories();
		
		List<IFood> foods = new ArrayList<>(new SwissDB().parseDB());

		calculateAlternatives(target, foods);
		
	}


	private static void calculateAlternatives(NutritionTarget target, List<IFood> foods) {
		List<Restriction<IFood>> extraRestrictions = new ArrayList<>();

		List<IFood> base = new FoodOptimize().optimize(foods,
				extraRestrictions, target);
		Double basePrice = base.stream().mapToDouble(IFood::getPrice).sum();
		
		System.out.println("The base price is "+basePrice+ " adding "+FACTOR +" of ");
		
		
		target.target.keySet().parallelStream().map(n->{
			
			NutritionTarget modifiedTarget = target.clone();
			
			modifiedTarget.set(n, modifiedTarget.get(n).mult(FACTOR));
			List<IFood> optimal = new FoodOptimize().optimize(foods,
					extraRestrictions, modifiedTarget);
			
			AltSzenario alt = new AltSzenario();
			alt.nutrient = n;
			alt.price =  optimal.stream().mapToDouble(IFood::getPrice).sum();
			return alt;		
		})
		.sorted(Comparator.comparing(a->-a.price))
		.forEachOrdered(a->a.print(basePrice));
	}
	
	
	private static class AltSzenario{
		public Nutrient nutrient;
		public Double price;
		
		public void print(double baseprice){
			Double diff = price-baseprice;			
			System.out.println(String.format("%20s costs %3.3f. Thats %+3.3f more than the base.", nutrient.name(), price, diff));
		}
		
	}

}
