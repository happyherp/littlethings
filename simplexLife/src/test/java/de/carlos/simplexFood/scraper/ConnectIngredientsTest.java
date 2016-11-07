package de.carlos.simplexFood.scraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.scraper.RezepteEu.Ingredient;
import de.carlos.simplexFood.scraper.RezepteEu.Recipie;
import de.carlos.simplexFood.swissDB.SwissDB;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.QGram;

public class ConnectIngredientsTest {

	@Test
	public void testConvertIngredient() {
		List<IFood> ingredientList = new ArrayList<>();
		Food food = new Food();
		food.setName("Mehl");
		ingredientList.add(food);
		
		Ingredient i = new Ingredient();
		i.amount = 100.0;
		i.unit = "g";
		i.name = "Mehl";
		
		IFood asFood = new ConnectIngredients().convertIngredient(i, ingredientList);
		Assert.assertEquals("Mehl", asFood.getName());
	}
	
	
	@Test
	public void testPick() {
		List<IFood> ingredientList = new ArrayList<>();
		
		Food food = new Food();
		food.setName("Mehl, roggen");
		ingredientList.add(food);
		
		food = new Food();
		food.setName("Mehl, weizen");
		ingredientList.add(food);
		
		food = new Food();
		food.setName("Mehl");
		ingredientList.add(food);
		
		Ingredient i = new Ingredient();
		i.amount = 100.0;
		i.unit = "g";
		i.name = "Mehl, ro";
		
		IFood asFood = new ConnectIngredients().convertIngredient(i, ingredientList);
		Assert.assertEquals("Mehl, roggen", asFood.getName());
		
		i = new Ingredient();
		i.amount = 100.0;
		i.unit = "g";
		i.name = "Weizenmehl";
		
		asFood = new ConnectIngredients().convertIngredient(i, ingredientList);
		Assert.assertEquals("Mehl, weizen", asFood.getName());
	}
	
	@Test
	public void testStrDistanceQgram(){
		QGram qGram = new QGram(3);
		Assert.assertEquals(0.0, qGram.distance("Weizen", "Weizen"), 0.0);
		Assert.assertEquals(11.0, qGram.distance("Mehl aus Weizen", "Weizenmehl"), 0.0);
		Assert.assertEquals(8.0, qGram.distance("Mehl", "Weizenmehl"), 0.0);
		Assert.assertEquals(19.0, qGram.distance("Mehl aus Roggen", "Weizenmehl"), 0.0);
		Assert.assertEquals(11.0, qGram.distance("Mehl aus Weizen", "Weizenmehl"), 0.0);
		
	}
	
	@Test
	public void testStrDistanceCosine(){
		Cosine cosine = new Cosine(3);
		Assert.assertEquals(0.0, cosine.distance("Weizen", "Weizen"), 0.0);
		Assert.assertEquals(0.5, cosine.distance("Mehl aus Weizen", "Weizenmehl"), 0.1);
		Assert.assertEquals(0.75, cosine.distance("Mehl", "Weizenmehl"), 0.0);
		Assert.assertEquals(0.9, cosine.distance("Mehl aus Roggen", "Weizenmehl"), 0.1);
		Assert.assertEquals(0.5, cosine.distance("Mehl aus Weizen", "Weizenmehl"), 0.1);
		
	}
	
	
	@Test
	public void testMatchAll(){
		
		
		List<Food> swissIngredients = new SwissDB().parseDB();
		
		List<Recipie> eu5Recipies = new RezepteEu().readFromFile();
		
		ConnectIngredients connectIngredients = new ConnectIngredients();
		
		eu5Recipies.stream()
		.flatMap(r->r.ingredients.stream())
		.forEach(i->{
			try{
				System.out.println(i.name+"->"+connectIngredients.convertIngredient(i, swissIngredients).getName() );
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		});
		

		
		
	}

}
