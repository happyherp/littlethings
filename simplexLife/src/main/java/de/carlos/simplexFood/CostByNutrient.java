package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexFood.swissDB.SwissDB;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class CostByNutrient {
	
	public static final double FACTOR = 1.1;
	
	public static void main(String[] args){
		
		
		NutritionTarget target = NutritionTarget.anyCalories();
		
		List<IFood> foods = new ArrayList<>(new SwissDB().parseDB());

		printAlternatives(target, foods);
		
	}


	private static void printAlternatives(NutritionTarget target, List<IFood> foods) {

		List<IFood> base = new FoodOptimize().optimize(foods, target);
		Double basePrice = base.stream().mapToDouble(IFood::getPrice).sum();
		
		System.out.println("The base price is "+basePrice+ " adding "+FACTOR +" of ");
		
		
		target.target.keySet().parallelStream().map(n->buildAlt(target, n, foods))
		.sorted(Comparator.comparing(a->-a.price))
		.forEachOrdered(a->a.print(basePrice));
	}
	
	public static AltSzenario buildAlt(NutritionTarget original, Nutrient toChange, List<IFood> foods){

		NutritionTarget modifiedTarget = original.clone();
		
		modifiedTarget.set(toChange, modifiedTarget.get(toChange).mult(FACTOR));
		List<IFood> optimal = new FoodOptimize().optimize(foods,
				new ArrayList<>(), modifiedTarget);
		
		AltSzenario alt = new AltSzenario();
		alt.nutrient = toChange;
		alt.price =  optimal.stream().mapToDouble(IFood::getPrice).sum();
		return alt;		
	}
	
	
	public static class AltSzenario{
		public Nutrient nutrient;
		public Double price;
		
		public void print(double baseprice){
			Double diff = price-baseprice;			
			System.out.println(String.format("%20s costs %3.3f. Thats %+3.3f more than the base.", nutrient.name(), price, diff));
		}
		
	}

}
