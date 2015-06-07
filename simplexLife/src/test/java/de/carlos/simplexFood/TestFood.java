package de.carlos.simplexFood;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestFood {
    
    @Test
    public void test(){
	
	
    	List<Food> foods = new SwissDB().parseDB();
    	
    	//foods = foods.subList(0, 1000);
    	
    	Map<IFood, Double> result = new FoodOptimize().optimize(foods);
    	

    	Meal meal = new Meal(result);
    	System.out.println("Selected Foods.");
    	double preis_gesamt = 0.0;
    	for (IFood f : result.keySet()){
    		double p =  f.getPrice() * result.get(f); 
    		System.out.print(String.format("%-40s %8.3fg %4.2f€",f.getName() ,result.get(f)*100, p));
    		FoodOptimize.printPercentages(f, meal);
    		System.out.println("");
    		preis_gesamt += p;
    	}
    	System.out.println(String.format("Gesamtpreis Tagesbedarf: %4.2f€",preis_gesamt));

    }
    
    @Test
    public void printByAttr(){
    	List<Food> foods = new SwissDB().parseDB();
    	
    	FoodOptimize.printByAttr(foods, f->f.getVitaminC());

    }

}
