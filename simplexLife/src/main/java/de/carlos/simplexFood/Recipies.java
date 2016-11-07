package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Meal;
import de.carlos.simplexFood.food.Nutrient;

public class Recipies {
	
	
	
	private Collection<? extends IFood> allfoods;
	public  Meal lasagne;
	public IFood hackGemischt;
	public Meal brot;
	public Food water;
	public Meal riegel;
	public List<IFood> vitaminSubsets;

	public Recipies(List<? extends IFood> foods){
		this.allfoods = foods;
		this.hackGemischt = this.findByName("Hackfleisch gemischt (Migros)");
		this.water = new Food();
		this.water.setName("Wasser");
		this.water.setPrice(0.0);
		
//		this.lasagne = new Meal()
//		.addIngredient(this.hackGemischt.gram(250))
//		.addIngredient(this.findByName("tomatenmark"));
		
		this.brot = new Meal()
		   .addIngredient(this.findByName("Weizenmehl, Ruch, Typ 1100").gram(500))
		   .addIngredient(this.water.gram(350))
		   .addIngredient(this.findByName("Trockenhefe (Dr. Oetker)").gram(7))
		   .addIngredient(this.findByName("Zucker, weiss").el(1))
		   .addIngredient(this.findByName("Distelöl").el(2))
		   .addIngredient(this.findByName("Kochsalz, jodiert").el(1));
		
		
		// http://www.chefkoch.de/rezepte/283921106784138/Vollwertige-Muesliriegel.html
		this.riegel = new Meal()
			.addIngredient(this.findByName("Weizenmehl, weiss, Typ 400").gram(300))
			.addIngredient(this.findByName("Haferflocken").gram(200))
			.addIngredient(this.findByName("Kokosnuss, getrocknet (Kokosrapseln, Kokosflocken)").gram(200))
			.addIngredient(this.findByName("Zucker, weiss").gram(150))
			//Statt Honig
			.addIngredient(this.findByName("Rama Universelle Margarine (Unilever)").gram(120))
			.addIngredient(this.water.gram(180))
			.addIngredient(this.findByName("Sesamsamen").el(4));
		
		this.brot.setName("Eigenes Brot");
		
		this.vitaminSubsets = createVitaminSubsets();
		
	}
	
	private List<IFood> createVitaminSubsets() {
		
		
		IFood original = this.findByName("A-Z Tabletten DocMorris 1G=1Kapsel");
		List<IFood> subsets = new ArrayList<>();
		
		for (Nutrient n : Nutrient.values()){
			if (original.getNutrient(n) > 0.0){
				Food subset = new Food();
				//subset.setPrice(original.getPrice() / 10);
				subset.setPrice(0.01);
				subset.setName("Subset DocMorris "+n);
				subset.setNutrient(n, original.getNutrient(n));
				subsets.add(subset);
			}
			
		}
		return subsets;
	}

	private IFood findByName(String name){
		return this.allfoods.stream().filter(f->f.getName().equals(name)).findFirst().get();
	}

}
