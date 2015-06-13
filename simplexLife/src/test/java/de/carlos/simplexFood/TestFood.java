package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import de.carlos.simplexOO.SimplexOO.Restriction;

public class TestFood {
    
    @Test
    public void test(){
	
	
    	List<Food> foods = new SwissDB().parseDB(); 
    	
    	foods = foods.stream()
    	//		.filter(f->!f.getName().contains("mehl"))
    			.collect(ArrayList::new, ArrayList::add,ArrayList::addAll);
    	
    	
    	List<Restriction<IFood>> extraRestr = new ArrayList<>();
    	
//    	IFood apple = foods.stream().filter(f->f.getName().contains("Apfel, roh")).findFirst().get();
//    	extraRestr.add(SimplexOO.atLeast(2, apple));
    	
    	Collection<IFood> result = new FoodOptimize().optimize(foods, extraRestr);
    	

    	System.out.println("Selected Foods.");
    	for (IFood f : result){
    		System.out.print(String.format("%-40s %8.3fg %4.2f€",f.getName() ,f.getWeight(), f.getPrice()));
    		//FoodOptimize.printPercentages(f, meal);
    		System.out.println("");

    	}
    	double preis_gesamt = result.stream().map(IFood::getPrice).reduce(0.0, (a,b)->a+b);
    	System.out.println(String.format("Gesamtpreis Tagesbedarf: %4.2f€",preis_gesamt));

    	
    }
    
    @Test
    public void printByAttr(){
    	List<Food> foods = new SwissDB().parseDB();
    	
    	FoodOptimize.printByAttr(foods, IFood::getFatSaturated);

    }

}
