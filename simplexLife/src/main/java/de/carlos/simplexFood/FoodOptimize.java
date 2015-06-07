package de.carlos.simplexFood;

import java.lang.reflect.Field;
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

import de.carlos.simplexOO.SimplexOO;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class FoodOptimize {

	public Map<IFood, Double> optimize(List<? extends IFood> foods) {
		
		List<Restriction<IFood>> constraints = new ArrayList<>();
		// Basic Elements
		constraints.add(new Restriction<IFood>(f -> f.getKohlenhydrate(), Relationship.EQ, 340.0));
		constraints.add(new Restriction<IFood>(f -> f.getFett(), Relationship.EQ, 80.0));
		constraints.add(new Restriction<IFood>(f -> f.getProtein(), Relationship.EQ, 60.0));
		constraints.add(new Restriction<IFood>(f -> f.getBallast(), Relationship.EQ, 25.0));

		// Spurenelemente
		constraints.add(new Restriction<IFood>(f -> f.getCalcium(), Relationship.GEQ, 1.0));
		constraints.add(new Restriction<IFood>(f -> f.getEisen(), Relationship.GEQ, 10.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getIod(), Relationship.GEQ, 200.0E-6));
		constraints.add(new Restriction<IFood>(f -> f.getFluorid(), Relationship.GEQ, 3.8E-3));
		constraints.add(new Restriction<IFood>(f -> f.getMagnesium(), Relationship.GEQ, 350.0E-3));
		constraints.add(new Restriction<IFood>(f -> f.getZink(), Relationship.GEQ, 10.0E-3));

		// Vitamine
		constraints.add(new Restriction<IFood>(f -> f.getVitaminA(), Relationship.GEQ, 1.0E-6));
		constraints.add(new Restriction<IFood>(f -> f.getVitaminB1(), Relationship.GEQ, 1.2E-3));
		constraints.add(new Restriction<IFood>(f -> f.getVitaminB2(), Relationship.GEQ, 1.4E-3));
		constraints.add(new Restriction<IFood>(f -> f.getVitaminB6(), Relationship.GEQ, 1.5E-3));
		constraints.add(new Restriction<IFood>(f -> f.getVitaminB12(), Relationship.GEQ, 3E-6));
		constraints.add(new Restriction<IFood>(f -> f.getVitaminC(), Relationship.GEQ, 100E-3));
		constraints.add(new Restriction<IFood>(f -> f.getVitaminD(), Relationship.GEQ, 5E-6));

		constraints.add(new Restriction<IFood>(f -> f.getVitaminE(), Relationship.GEQ, 14E-3));
		constraints.add(new Restriction<IFood>(f -> f.getVitaminE(), Relationship.LEQ, 300E-3));

		constraints.add(new Restriction<IFood>(f -> f.getNiacin(), Relationship.GEQ, 16E-3));

		constraints.add(new Restriction<IFood>(f -> f.getFolat(), Relationship.GEQ, 400E-6));	
		
		
		//Unavailable
//		constraints.addC(f -> f.selen
//				, Relationship.GEQ, 40.0E-6);			 
		

		Map<IFood, Double> result = new SimplexOO<IFood>().solve(
				(Collection<IFood>) Arrays.asList(filterValid(foods).toArray(i->new IFood[i])), 
				constraints, 
						f->f.getPrice(), 
						GoalType.MINIMIZE);

		return result;
	}

	private Stream<? extends IFood> filterValid(List<? extends IFood> foods) {
		Stream<? extends IFood> valid = foods.stream().filter(p -> p.getPrice() != null);
		return valid;
	}



	private static Method[] findDoubleGetMethods() {
		Method[] attributes = Arrays.stream(IFood.class.getMethods())
				.filter(f -> f.getReturnType() == double.class)
				.toArray(a->new Method[a]);
				
		return attributes;
	}
	
	private static Method[] findDoubleSetMethods() {
		Method[] attributes = Arrays.stream(IFood.class.getMethods())
				.filter(f -> f.getParameterCount() == 1 && f.getParameterTypes()[0] == double.class)
				.toArray(a->new Method[a]);
				
		return attributes;
	}
	
	public static void printPercentages(IFood f, IFood sum) {

		Method[] sortedFields = findDoubleGetMethods();
		Arrays.sort(sortedFields, new Comparator<Method>() {

			@Override
			public int compare(Method o1, Method o2) {
				return -Double.compare(getPercent(f, sum, o1),
						getPercent(f, sum, o2));
			}
		});

		for (Method method : sortedFields) {
			System.out.print(String.format(" %s(x%2.0f%%) ", method.getName()
					.substring(3), getPercent(f, sum, method)));
		}

	}

	private static double getPercent(IFood f, IFood all, Method field){
		try {
		double thisD = (double)field.invoke(f);
		double avgD = (double)field.invoke(all); 
		double factor = 100*thisD/avgD;
		return factor;
	} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
		throw new RuntimeException(e);
	}
	}

	public static void printByAttr(List<Food> foods, ToDoubleFunction<Food> f) {

    	foods.stream().sorted(new Comparator<Food>(){

			@Override
			public int compare(Food o1, Food o2) {
				return Double.compare(f.applyAsDouble(o1), f.applyAsDouble(o2));
			}
    		
    	}).forEachOrdered(food -> System.out.println(food.getName() + " "+ f.applyAsDouble(food)));
	
   		
	}

}
