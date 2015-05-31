package de.carlos.simplexFood;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestFood {
    
    @Test
    public void test(){
	
	
    	List<Food> foods = new SwissDB().parseDB();
    	
    	//foods = foods.subList(0, 1000);
    	
    	Map<Food, Double> result = new FoodOptimize().optimize(foods);
    	
    	System.out.println("Selected Foods.");
    	double preis_gesamt = 0.0;
    	for (Food f : result.keySet()){
    		double p =  f.price * result.get(f); 
    		System.out.println(String.format("%-40s %8.3fg %4.2f€",f.name ,result.get(f)*100, p));
    		preis_gesamt += p;
    	}
    	System.out.println(String.format("Gesamtpreis Tagesbedarf: %4.2f€",preis_gesamt));
	
	
    	
    	
    	
    }

}
