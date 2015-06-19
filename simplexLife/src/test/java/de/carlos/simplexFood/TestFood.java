package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class TestFood {
    
    @Test
    public void test(){
	
	
    	List<IFood> foods = new ArrayList<>(new SwissDB().parseDB()); 
    	
    	NutritionTarget target = NutritionTarget.dailyMale();
    	
    	Recipies recipies = new Recipies(foods);
    	foods.add(recipies.brot);
    	
    	
    	foods = foods.stream()
    	//		.filter(f->!f.getName().contains("mehl"))
    			.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
    	
    	
    	List<Restriction<IFood>> extraRestr = new ArrayList<>();
    	
//    	IFood apple = foods.stream().filter(f->f.getName().contains("Apfel, roh")).findFirst().get();
//    	extraRestr.add(SimplexOO.atLeast(2, apple));
    	
    	List<IFood> result = new FoodOptimize().optimize(foods, extraRestr, target);
		result.sort((a,b)->(int) (b.getWeight() - a.getWeight()));


    	FoodOptimize.printSummary(result);

    	
    }
    
    @Test
    public void printByAttr(){
    	List<Food> foods = new SwissDB().parseDB();
    	
    	FoodOptimize.printByAttr(foods, Nutrient.VitaminB12);

    }
    
    @Test
    public void testInsert(){
    	
    	Food food = new Food();
    	food.setName("testfood");
    	
    	HibernateUtil.beginTransaction();
    	HibernateUtil.getSession().save(food);
    	
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
   

}
