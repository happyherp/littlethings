package de.carlos.simplexFood.food;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.ToDoubleFunction;

public class Meal implements IFood{
	
	private String name;
	
	private Collection<IFood> ingredients = new ArrayList<>();
	
	public Meal(){
	}
	
	public Meal(Collection<IFood> ingredients) {
		this.ingredients = ingredients;
	}

	public Meal addIngredient(IFood food){
		this.ingredients.add(food);
		return this;
	}
	
	private double sumField(ToDoubleFunction<IFood> acessor){
		return ingredients.stream().map(f->acessor.applyAsDouble(f)).reduce(0.0,(a, b)->a+b);
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setName(String n){
		this.name = n;
	}
	

	@Override
	public Double getPrice() {
		return sumField(f->f.getPrice());

	}

	@Override
	public double getWeight() {
		return sumField(f->f.getWeight());

	}


	@Override
	public Double getNutrient(Nutrient i) {
		
		double sum = 0.0;
		
		for (IFood ingredient: this.ingredients){
			if (ingredient.getNutrient(i) != null){
				sum += ingredient.getNutrient(i);
			}
		}
		
		return sum;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Meal){
			return this.ingredients.equals(((Meal) o).ingredients);
		}else{
			return super.equals(o);
		}
	}

}
