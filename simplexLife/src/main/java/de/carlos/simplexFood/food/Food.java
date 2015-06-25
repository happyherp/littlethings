package de.carlos.simplexFood.food;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;



@Entity()
public class Food implements IFood {
    
	private int id;
    
    private String name = "nameless";
    
    private double weight = 100.0;
    
    private Double price = null;    
    
    private Map<Nutrient, FoodNutrient> nutrients = new HashMap<>();
    
    public Double getNutrient(Nutrient i){
    	
    	FoodNutrient fd = this.nutrients.get(i);
    	if (fd == null){
    		return 0.0;
    	} 
    	
    	return fd.getAmount();
    }
    
    public void setNutrient(Nutrient i, Double d){
    	FoodNutrient fn = new FoodNutrient();
    	fn.setAmount(d);
    	fn.setNutrient(i);
    	fn.setFood(this);
    	this.nutrients.put(i, fn);
    }
    

    
    public Food() {
    }
        


    @OneToMany(mappedBy="food")
    @MapKey(name="nutrient")
    public Map<Nutrient, FoodNutrient> getNutrients() {
		return nutrients;
	}

	public void setNutrients(Map<Nutrient, FoodNutrient> nutrients) {
		this.nutrients = nutrients;
	}

	@Id
	@GeneratedValue()
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
	
	public boolean equals(Object o){
		if (o instanceof Food){
			return this.name.equals(((Food) o).name) && this.weight == ((Food) o).weight;
		}else{
			return super.equals(o);
		}
	}

    
}
