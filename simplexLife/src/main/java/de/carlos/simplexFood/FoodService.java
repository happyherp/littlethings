package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;

import de.carlos.simplexFood.NutritionTarget.Limit;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexFood.swissDB.SwissDB;

public class FoodService {
	
	List<IFood> allFoods = new ArrayList<>(new SwissDB().parseDB());
	
	public List<IFood> run(NutritionTarget target, List<IFood> excluded){

		List<IFood> result = new FoodOptimize().optimize(getFood(excluded), new ArrayList<>(), target);
		result.sort(new IFood.WeightComparator());
		
		
		return result;
	}
	
	public List<IFood> getFood( List<IFood> excluded){
		List<IFood> foods = new ArrayList<>(allFoods);
		foods.removeAll(excluded);
		return foods;
	}
	
	public List<IFood> makeExcluded(ServletRequest request){
		return allFoods.stream()
				.filter(f->request.getParameter("exclude_"+f.getName()) != null)
				.collect(Collectors.toList());

	}
	
	public NutritionTarget makeTarget(ServletRequest request){
		NutritionTarget target;
		if(request.getParameter("start")==null){
			target = NutritionTarget.dailyMale();
			Arrays.stream(Nutrient.values())
				.forEach(n-> target.target.putIfAbsent(n, new Limit(null)));
		}else{
			target = new NutritionTarget();
			for (Nutrient n: Nutrient.values()){
				target.set(n, 
						readDouble(request.getParameter("min_"+n.name())), 
						readDouble(request.getParameter("max_"+n.name())));
			}
		}
		return target;
	}

	private Double readDouble(String string) {
		if (string == null || string.isEmpty()){
			return null;
		}
		return Double.parseDouble(string);
	}

}
