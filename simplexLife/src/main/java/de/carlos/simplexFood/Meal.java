package de.carlos.simplexFood;

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
	public double getFolat() {
		return sumField(f->f.getFolat());
	}

	@Override
	public double getNiacin() {
		return sumField(f->f.getNiacin());

	}

	@Override
	public double getVitaminE() {
		return sumField(f->f.getVitaminE());
	}

	@Override
	public double getVitaminC() {
		return sumField(f->f.getVitaminC());
	}

	@Override
	public double getVitaminD() {
		return sumField(f->f.getVitaminD());

	}

	@Override
	public double getVitaminB6() {
		return sumField(f->f.getVitaminB6());

	}

	@Override
	public double getVitaminB12() {
		return sumField(f->f.getVitaminB12());

	}

	@Override
	public double getVitaminB2() {
		return sumField(f->f.getVitaminB2());
	}

	@Override
	public double getVitaminB1() {
		return sumField(f->f.getVitaminB1());

	}

	@Override
	public double getVitaminA() {
		return sumField(f->f.getVitaminA());

	}

	@Override
	public double getZink() {
		return sumField(f->f.getZink());

	}

	@Override
	public double getMagnesium() {
		return sumField(f->f.getMagnesium());

	}

	@Override
	public double getFluorid() {
		return sumField(f->f.getFluorid());

	}

	@Override
	public double getIod() {
		return sumField(f->f.getIod());
	}

	@Override
	public double getEisen() {
		return sumField(f->f.getEisen());
	}

	@Override
	public double getCalcium() {
		return sumField(f->f.getCalcium());

	}

	@Override
	public double getBallast() {
		return sumField(f->f.getBallast());
	}

	@Override
	public double getProtein() {
		return sumField(f->f.getProtein());

	}

	@Override
	public double getFett() {
		return sumField(f->f.getFett());

	}

	@Override
	public double getKohlenhydrate() {
		return sumField(f->f.getKohlenhydrate());

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
	public double getBetaCarotene() {
		return sumField(f->f.getBetaCarotene());
	}

	public double getPantothenicAcid() {
		return sumField(f->f.getPantothenicAcid());
	}

	@Override
	public double getSodium() {
		return sumField(f->f.getSodium());
	}
	

	public double getPotassium() {
		return sumField(f->f.getPotassium());
	}
	
	public double getChloride() {
		return sumField(f->f.getChloride());
	}

	public double getFatPolyUnsaturated() {
		return sumField(f->f.getFatPolyUnsaturated());
	}

	public double getFatMonoUnsaturated() {
		return sumField(f->f.getFatMonoUnsaturated());
	}

	public double getFatSaturated() {
		return sumField(f->f.getFatSaturated());
	}

}
