package de.carlos.simplexFood.food;

import java.util.Comparator;


public interface IFood {

	public abstract Double getPrice();

	public abstract String getName();
	
	
	/**
	 * Return weight in Grams. 
	 * @return
	 */
	public abstract double getWeight();
	
	/**
	 * Create a new Food which is the same as this, but multiplied with the
	 * given factor.
	 * 
	 * @param factor
	 * @return
	 */
	public default IFood mult(double factor) {

		Food fNew = new Food();
		for (Nutrient n: Nutrient.values()) {
			
			if (this.getNutrient(n) != null){
				fNew.setNutrient(n, this.getNutrient(n) * factor);
			}
		}

		fNew.setName(this.getName());
		if (this.getPrice() != null) {
			fNew.setPrice(this.getPrice() * factor);
		}
		fNew.setWeight(this.getWeight() * factor);
		return fNew;

	}
  

	public default IFood gram(double gram){
		return this.mult(gram / this.getWeight());
	}
	
	public default IFood ml(double ml){
		//TODO: instead use a ml-gram ratio
		return this.gram(ml);
	}

	/**
	 * 
	 * 1 EL -> 10 -15g
	 * @param i
	 * @return
	 */
	public default IFood el(int i){
		return this.gram(i*12);
	}
	
	public default IFood tl(int i){
		return this.gram(i*4);
	}

	
	
	public default Food free(){
		Food f = (Food) this.mult(1.0);
	    f.setPrice(0.0);
	    return f;
	}
	
    public Double getNutrient(Nutrient i);

    public class WeightComparator implements Comparator<IFood>{
    	
    	@Override
    	public int compare(IFood arg0, IFood arg1) {
    		return Double.compare(arg1.getWeight(), arg0.getWeight());
    	}
    }
    
    public boolean isEdible();


}
