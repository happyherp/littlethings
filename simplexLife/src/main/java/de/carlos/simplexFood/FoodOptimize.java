package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Meal;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexOO.SimplexOO;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class FoodOptimize {

	public List<IFood> optimize(List<? extends IFood> foods, Collection<Restriction<IFood>> extraRestrictions, NutritionTarget target) {
		
		List<Restriction<IFood>> constraints = new ArrayList<>();
		constraints.addAll(extraRestrictions);
		constraints.addAll(target.createRestrictions());
		
		
		double precisionFactor = 1.0E-0;
		
		Map<IFood, Double> result = 
			new SimplexOO<IFood>(SimplexOO.DEFAULT_EPSILON*precisionFactor,SimplexOO.DEFAULT_ULPS, SimplexOO.DEFAULT_CUT_OFF* precisionFactor)
				.solve(
				(Collection<IFood>) Arrays.asList(filterValid(foods).toArray(i->new IFood[i])), 
				constraints, 
						f->f.getPrice(), 
						GoalType.MINIMIZE);
		
		
		List<IFood> resultFood = new ArrayList<IFood>();
		for (Map.Entry<IFood, Double> e: result.entrySet()){
			resultFood.add(e.getKey().mult(e.getValue()));
		}
		
		Meal m = new Meal(resultFood);
		if (!target.matches(m)){
			throw new RuntimeException("Food did not match target. Miscalculation in Simplex.");
		}
		

		return resultFood;
	}

	private Stream<? extends IFood> filterValid(List<? extends IFood> foods) {
		Stream<? extends IFood> valid = foods.stream().filter(p -> p.getPrice() != null);
		return valid;
	}


	
	public static void printPercentages(IFood f, IFood sum) {

		Nutrient[] sortedFields = Arrays.copyOf(Nutrient.values(), Nutrient.values().length);
		Arrays.sort(sortedFields, new Comparator<Nutrient>() {

			@Override
			public int compare(Nutrient o1, Nutrient o2) {
				return -Double.compare(getPercent(f, sum, o1),
						getPercent(f, sum, o2));
			}
		});

		for (Nutrient method : sortedFields) {
			double p = getPercent(f, sum, method);
			if (p > 1) {
				System.out.print(String.format(" %s(%2.0f%%) ", method, p));
			}
		}

	}

	private static double getPercent(IFood f, IFood all, Nutrient field) {
		double thisD = f.getNutrient(field);
		double avgD = all.getNutrient(field);
		double factor = 100 * thisD / avgD;
		return factor;

	}

	public static void printByAttr(List<? extends IFood> foods, Nutrient n) {

    	foods.stream()
    	   .sorted((o1, o2)-> Double.compare(o1.getNutrient(n)/ o1.getPrice(), o2.getNutrient(n) / o2.getPrice()))
		   .forEachOrdered(food -> System.out.println(food.getName() + " "+food.getNutrient(n) / food.getPrice()));
	   		
	}

	public static void printSummary(Collection<IFood> result) {
    	System.out.println("Selected Foods.");
    	int i = 1;
    	for (IFood f : result){
    		System.out.print(String.format("%2d:%-50s %8.3fg %4.2f€",i, f.getName() ,f.getWeight(), f.getPrice()));
    		FoodOptimize.printPercentages(f, new Meal(result));
    		System.out.println("");
    		i++;

    	}
    	double preis_gesamt = result.stream().map(IFood::getPrice).reduce(0.0, (a,b)->a+b);
    	System.out.println(String.format("Gesamtpreis Tagesbedarf: %4.2f€",preis_gesamt));		
    	
    	
	}

	

}
