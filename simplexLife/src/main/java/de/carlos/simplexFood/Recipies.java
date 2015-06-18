package de.carlos.simplexFood;

import java.util.Collection;
import java.util.List;

public class Recipies {
	
	
	
	private Collection<? extends IFood> allfoods;
	public  Meal lasagne;
	public IFood hackGemischt;
	public Meal brot;
	public Food water;

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
		this.brot.setName("Eigenes Brot");
	}
	
	private IFood findByName(String name){
		return this.allfoods.stream().filter(f->f.getName().equals(name)).findFirst().get();
	}

}
