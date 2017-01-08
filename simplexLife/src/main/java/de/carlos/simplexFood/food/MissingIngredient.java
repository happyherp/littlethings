package de.carlos.simplexFood.food;

public class MissingIngredient implements IFood {
	
	
	private Nutrient nutrient;

	public MissingIngredient(Nutrient n) {
		this.nutrient = n;
	}

	@Override
	public Double getPrice() {
		return 0.0;
	}

	@Override
	public String getName() {
		return "Missing "+this.nutrient.name();
	}

	@Override
	public double getWeight() {
		return 1.0;
	}

	@Override
	public Double getNutrient(Nutrient i) {
		return i == nutrient ? 1.0 : 0 ;
	}

	@Override
	public boolean isEdible() {
		return false;
	}

}
