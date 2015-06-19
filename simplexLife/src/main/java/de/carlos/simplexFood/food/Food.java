package de.carlos.simplexFood.food;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity()
public class Food implements IFood {
    
	private int id;
    
    private String name = "nameless";
    
    private double weight = 100.0;
    
    private Double price = null;    
    
    private Map<Nutrient, Double> nutrients = new HashMap<>();
    
    public Double getNutrient(Nutrient i){
    	return this.nutrients.get(i);
    }
    
    public void setNutrient(Nutrient i, Double d){
    	this.nutrients.put(i, d);
    }
    

    
    public Food() {
    }


    @Id
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	@Override
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}



	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}

    
}
