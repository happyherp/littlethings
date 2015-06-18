package de.carlos.simplexFood;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import static de.carlos.simplexFood.Nutrient.*;

import de.carlos.simplexOO.SimplexOO;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class FoodOptimize {

	public List<IFood> optimize(List<? extends IFood> foods, Collection<Restriction<IFood>> extraRestrictions) {
		
		List<Restriction<IFood>> constraints = new ArrayList<>();
		constraints.addAll(extraRestrictions);
		
		// Basic Elements
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Kohlenhydrate), Relationship.EQ, 340.0));
		//constraints.add(new Restriction<IFood>(f -> f.getNutrient(Fett), Relationship.EQ, 80.0));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(FatSaturated), Relationship.EQ, 30.0));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(FatMonoUnsaturated), Relationship.EQ, 29.0));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(FatPolyUnsaturated), Relationship.EQ, 21.0));		
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Protein), Relationship.EQ, 60.0));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Ballast), Relationship.EQ, 25.0));

		// Spurenelemente
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Calcium), Relationship.GEQ, 1.0));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Eisen), Relationship.GEQ, 10.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Eisen), Relationship.LEQ, 45.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Iod), Relationship.GEQ, 200.0E-6));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Iod), Relationship.LEQ, 500.0E-6));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Fluorid), Relationship.GEQ, 3.8E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Magnesium), Relationship.GEQ, 350.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Zink), Relationship.GEQ, 10.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Zink), Relationship.LEQ, 30.0E-3));

		// Vitamine
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminA), Relationship.GEQ, 1.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminA), Relationship.LEQ, 3.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(BetaCarotene), Relationship.GEQ, 2.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(BetaCarotene), Relationship.LEQ, 10.0E-3));		
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminB1), Relationship.GEQ, 1.2E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminB2), Relationship.GEQ, 1.4E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminB6), Relationship.GEQ, 1.5E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminB12), Relationship.GEQ, 3E-6));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminC), Relationship.GEQ, 100E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminD), Relationship.GEQ, 5E-6));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminE), Relationship.GEQ, 14E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(VitaminE), Relationship.LEQ, 300E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Niacin), Relationship.GEQ, 16E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Niacin), Relationship.LEQ, 32E-3));
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Folat), Relationship.GEQ, 400E-6));	
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(PantothenicAcid), Relationship.GEQ, 6E-3));	
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Sodium), Relationship.GEQ, 500E-3));	
		constraints.add(new Restriction<IFood>(f -> f.getNutrient(Chloride), Relationship.GEQ, 2));	
		
		
		//Unavailable
//		constraints.addC(f -> f.selen
//				, Relationship.GEQ, 40.0E-6);			 
		

		Map<IFood, Double> result = new SimplexOO<IFood>().solve(
				(Collection<IFood>) Arrays.asList(filterValid(foods).toArray(i->new IFood[i])), 
				constraints, 
						f->f.getPrice(), 
						GoalType.MINIMIZE);
		
		
		List<IFood> resultFood = new ArrayList<IFood>();
		for (Map.Entry<IFood, Double> e: result.entrySet()){
			resultFood.add(e.getKey().mult(e.getValue()));
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
			if (p > 5) {
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

	public static void printByAttr(List<Food> foods, Nutrient n) {

    	foods.stream()
    	   .sorted((o1, o2)-> Double.compare(o1.getNutrient(n)/ o1.getPrice(), o1.getNutrient(n) / o2.getPrice()))
		   .forEachOrdered(food -> System.out.println(food.getName() + " "+food.getNutrient(n) / food.getPrice()));
	   		
	}

	public static void printSummary(Collection<IFood> result) {
    	System.out.println("Selected Foods.");
    	int i = 1;
    	for (IFood f : result){
    		System.out.print(String.format("%2d:%-40s %8.3fg %4.2f€",i, f.getName() ,f.getWeight(), f.getPrice()));
    		FoodOptimize.printPercentages(f, new Meal(result));
    		System.out.println("");
    		i++;

    	}
    	double preis_gesamt = result.stream().map(IFood::getPrice).reduce(0.0, (a,b)->a+b);
    	System.out.println(String.format("Gesamtpreis Tagesbedarf: %4.2f€",preis_gesamt));		
    	
    	
	}

	

}
