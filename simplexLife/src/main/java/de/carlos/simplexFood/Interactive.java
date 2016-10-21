package de.carlos.simplexFood;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.swissDB.SwissDB;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class Interactive {

	public void start(List<? extends IFood> available, NutritionTarget target) {

		try {
			List<Restriction<IFood>> extraRestrictions = new ArrayList<>();
			BufferedReader lineIn = new BufferedReader(new InputStreamReader(
					System.in));

			while (true) {
				List<IFood> result = new FoodOptimize().optimize(available,
						extraRestrictions, target);
				result.sort(new IFood.WeightComparator());
				FoodOptimize.printSummary(result);

				System.out.println("Choose food to be removed:");
				IFood toRemove = result
						.get(Integer.parseInt(lineIn.readLine()) - 1);
				available.remove(available.stream()
						.filter(f -> f.getName().equals(toRemove.getName()))
						.findFirst().get());
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {

		List<IFood> foods = new ArrayList<>(new SwissDB().parseDB());
		Recipies recipies = new Recipies(foods);
		//foods.addAll(recipies.vitaminSubsets);
		NutritionTarget target = NutritionTarget.dailyMale();

		new Interactive().start(foods, target);

	}

}
