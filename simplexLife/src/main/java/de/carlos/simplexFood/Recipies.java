package de.carlos.simplexFood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Recipe;
import de.carlos.simplexFood.food.Nutrient;

public class Recipies {
	
	
	
	private Collection<? extends IFood> ingredients;
	public List<Recipe> recipes = new ArrayList<>();
	public  Recipe lasagne;
	public IFood hackGemischt;
	public Recipe brot;
	public Food water;
	public Recipe riegel;
	public List<IFood> vitaminSubsets;

	public Recipies(List<? extends IFood> ingredients){
		this.ingredients = ingredients;
		
		this.hackGemischt = this.findByName("Hackfleisch gemischt (Migros)");
		IFood weizenmehl = this.findByName("Weizenmehl, Ruch, Typ 1100");
		IFood hefe = this.findByName("Trockenhefe (Dr. Oetker)");
		IFood zucker = this.findByName("Zucker, weiss");
		IFood disteloel = this.findByName("Distelöl");
		IFood salz = this.findByName("Kochsalz, jodiert");
		IFood tomatenmark = this.findByName("Tomatenpüree");
		
		this.water = new Food();
		this.water.setName("Wasser");
		this.water.setPrice(0.0);
		
		//http://www.chefkoch.de/rezepte/1112181217260303/Lasagne-Bolognese.html
		IFood olivenoel = this.findByName("M-Classic Olivenöl (Migros)");
		IFood karotte_stueck = this.findByName("Karotte, roh").gram(50);
		IFood stangensellerie_stueck = this.findByName("Stangensellerie, roh").gram(125);
		IFood knoblauchzehe = this.findByName("Knoblauch, roh").gram(6);
		IFood weisswein = this.findByName("Wein, weiss, 11 vol%");
		IFood gemuesebruehe = this.findByName("Bouillon, Gemüse, zubereitet");
		IFood butter = this.findByName("M-Budget Butter (Migros)");
		IFood milch = this.findByName("M-Budget Vollmilch UHT (Migros)");
		
		this.lasagne = newRecipe("Lasagne")
		.addIngredient(this.hackGemischt.gram(250))
		.addIngredient(tomatenmark.el(2))
		.addIngredient(this.findByName("Armando De Angelis Lasagneblätter frisch (Migros)"))
		.addIngredient(olivenoel.el(3))
		.addIngredient(karotte_stueck) // Eine
		.addIngredient(stangensellerie_stueck) // Eine
		.addIngredient(knoblauchzehe.mult(2))//2 zehen
		.addIngredient(weisswein.ml(200))
		.addIngredient(gemuesebruehe.ml(500))
		.addIngredient(butter.el(2))
		.addIngredient(weizenmehl.el(3))
		.addIngredient(milch.ml(500))
		.addIngredient(this.findByName("Parmesan").gram(100));
		
		
		
		
		this.brot = newRecipe("Eigenes Brot")
		   .addIngredient(weizenmehl.gram(500))
		   .addIngredient(this.water.gram(350))
		   .addIngredient(hefe.gram(7))
		   .addIngredient(zucker.el(1))
		   .addIngredient(disteloel.el(2))
		   .addIngredient(salz.el(1));
		
		
		// http://www.chefkoch.de/rezepte/283921106784138/Vollwertige-Muesliriegel.html
		IFood haferflocken = this.findByName("Haferflocken");
		IFood kokosflocken = this.findByName("Kokosnuss, getrocknet (Kokosrapseln, Kokosflocken)");
		IFood margarine = this.findByName("Rama Universelle Margarine (Unilever)");
		IFood sesamsamen = this.findByName("Sesamsamen");
		this.riegel = newRecipe("Müsliriegel")
			.addIngredient(weizenmehl.gram(300))
			.addIngredient(haferflocken.gram(200))
			.addIngredient(kokosflocken.gram(200))
			.addIngredient(zucker.gram(150))
			//Statt Honig
			.addIngredient(margarine.gram(120))
			.addIngredient(this.water.gram(180))
			.addIngredient(sesamsamen.el(4));
		
		
		
		
		this.vitaminSubsets = createVitaminSubsets();
		
	}
	
	private Recipe newRecipe(String name) {
		Recipe meal = new Recipe();
		meal.setName(name);
		this.recipes.add(meal);
		return meal;
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
		return this.ingredients.stream().filter(f->f.getName().equals(name)).findFirst().get();
	}

}
