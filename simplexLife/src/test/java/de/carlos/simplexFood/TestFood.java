package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Meal;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexFood.swissDB.SwissDB;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class TestFood {
    
    @Test
    public void test(){
	
	
    	List<IFood> foods = new ArrayList<>(new SwissDB().parseDB()); 
    	
    	NutritionTarget target = NutritionTarget.dailyMale();
    	
    	Recipies recipies = new Recipies(foods);
    	
    	Assert.assertTrue(
    			foods.stream().anyMatch(f->f.getName().contains("Blätterteigpastetli")));
    	
    	
    	//foods.addAll(recipies.vitaminSubsets);
    	
    	//IFood riegelKlein = recipies.riegel.gram(220);
    	//target = target.subtract(riegelKlein);    
    	//System.out.println("Riegelkosten "+riegelKlein.getPrice());
    	
    	//foods = foods.stream()
    	//		.filter(f->!f.getName().contains("mehl"))
    	//		.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
    	
    	
    	List<Restriction<IFood>> extraRestr = new ArrayList<>();
    	
//    	IFood apple = foods.stream().filter(f->f.getName().contains("Apfel, roh")).findFirst().get();
//    	extraRestr.add(SimplexOO.atLeast(2, apple));
    	
    	List<IFood> result = new FoodOptimize().optimize(foods, extraRestr, target);
		result.sort(new IFood.WeightComparator());


    	FoodOptimize.printSummary(result);    
    	
    	Meal m1 = new Meal(result);
    	Meal m2 = new Meal(new LinkedList(result));
    	Assert.assertEquals(m1, m2);
    	
    	
    	FoodOptimize.printByAttr(result, Nutrient.VitaminK);
    	
    	
    }
    
    @Test
    public void testLowCarb(){
	
	
    	List<IFood> foods = new ArrayList<>(new SwissDB().parseDB()); 
    	
    	NutritionTarget target = NutritionTarget.dailyMale();
    	target.set(Nutrient.Carbohydrates, null, 30.0);
    	target.set(Nutrient.Starch, null, null);
    	

    	List<Restriction<IFood>> extraRestr = new ArrayList<>();
    	
    	List<IFood> result = new FoodOptimize().optimize(foods, extraRestr, target);
		result.sort(new IFood.WeightComparator());


    	FoodOptimize.printSummary(result);    	
    }
    
    @Test
    public void testLowFat(){
	
	
    	List<IFood> foods = new ArrayList<>(new SwissDB().parseDB()); 
    	
    	NutritionTarget target = NutritionTarget.dailyMale();
    	target.set(Nutrient.FatTotal, null, 10.0);
    	target.set(Nutrient.FatPolyUnsaturated, null, null);
    	target.set(Nutrient.FatSaturated, null, null);
    	target.set(Nutrient.FatMonoUnsaturated, null, null);


    	List<Restriction<IFood>> extraRestr = new ArrayList<>();
    	
    	List<IFood> result = new FoodOptimize().optimize(foods, extraRestr, target);
		result.sort(new IFood.WeightComparator());


    	FoodOptimize.printSummary(result);    	
    }
    
    
    @Test
    public void printByAttr(){
    	List<Food> foods = new SwissDB().parseDB();
    	
    	FoodOptimize.printByAttr(foods, Nutrient.Protein);

    }
    
    @Test
    public void testInsert(){
    	
    	Food food = new Food();
    	food.setName("testfood");
    	
    	HibernateUtil.beginTransaction();
    	HibernateUtil.getSession().save(food);
    	HibernateUtil.flush();
    	
    }
    
    @Test
    public void testUnpriced(){
    	List<Food> foods = new SwissDB().parseDB(); 
    	
    	foods = foods.stream()
    			.filter(f->f.getPrice().equals(SwissDB.DEFAULT_PRICE))
    			.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
    	
    	
    	List<Restriction<IFood>> extraRestr = new ArrayList<>();
    	
//    	IFood apple = foods.stream().filter(f->f.getName().contains("Apfel, roh")).findFirst().get();
//    	extraRestr.add(SimplexOO.atLeast(2, apple));
    	
    	Collection<IFood> result = new FoodOptimize().optimize(foods, extraRestr, NutritionTarget.dailyMale());
    	

    	FoodOptimize.printSummary(result);

    }
    
    @Test
    public void testShuffle(){
	
    	NutritionTarget target = NutritionTarget.dailyMale();
	
    	List<IFood> foods = new ArrayList<>(new SwissDB().parseDB()); 
    	List<Restriction<IFood>> extraRestr = new ArrayList<>();
    	
    	
    	
    	List<IFood> result = new FoodOptimize().optimize(foods, extraRestr, target);
    	result.sort(new IFood.WeightComparator());
    	FoodOptimize.printSummary(result);    	
    	
    	Meal m1 = new Meal(result);
    	
    	
    	Collections.shuffle(foods);
    	
    	List<IFood> result2 = new FoodOptimize().optimize(foods, extraRestr, target);
    	result2.sort((a,b)->(int) (b.getWeight() - a.getWeight()));
    	FoodOptimize.printSummary(result);    	    	
    	
    	Meal m2 = new Meal(result2);
    	Assert.assertEquals(m1, m2);
    	

    }
   

}
