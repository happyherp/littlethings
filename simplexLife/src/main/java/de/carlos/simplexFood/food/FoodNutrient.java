package de.carlos.simplexFood.food;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class FoodNutrient {
	
	private int id;
	
	@Enumerated(EnumType.STRING)
	private Nutrient nutrient;
	
	private double amount;
	
	private Food food;
	

	@Id
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Nutrient getNutrient() {
		return nutrient;
	}


	public void setNutrient(Nutrient nutrient) {
		this.nutrient = nutrient;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	@ManyToOne(optional=false)
	public Food getFood() {
		return food;
	}


	public void setFood(Food food) {
		this.food = food;
	}
	
	public String toString(){
		return this.amount+"";
	}
	



}
