package de.carlos.simplexFood;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public interface IFood {

	public abstract double getFolat();

	public abstract double getNiacin();

	public abstract double getVitaminE();

	public abstract double getVitaminC();

	public abstract double getVitaminD();

	public abstract double getVitaminB6();

	public abstract double getVitaminB12();

	public abstract double getVitaminB2();

	public abstract double getVitaminB1();

	public abstract double getVitaminA();

	public abstract double getZink();

	public abstract double getMagnesium();

	public abstract double getFluorid();

	public abstract double getIod();

	public abstract double getEisen();

	public abstract double getCalcium();

	public abstract double getBallast();

	public abstract double getProtein();

	public abstract double getFett();

	public abstract double getKohlenhydrate();

	public abstract Double getPrice();

	public abstract String getName();
	
	public double getBetaCarotene();
	
	public double getSodium();
	
	public double getPotassium();
	
	public double getChloride();
	
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

		try {

			Method[] setter = FoodOptimize.findDoubleSetMethods();
			Method[] getter = FoodOptimize.findDoubleGetMethods();
			
			if (setter.length != getter.length){
				throw new RuntimeException("Numbe of getters was not equal to number of setters");
			}
			
			Comparator<Method> sortByName = new Comparator<Method>() {

				@Override
				public int compare(Method o1, Method o2) {
					return o1.getName().compareTo(o2.getName());
				}
			};
			Arrays.sort(setter, sortByName);
			Arrays.sort(getter, sortByName);
			
			//Sort the arrays by name, so they are aligned to each other. 
			
			Food fNew = new Food();
			for (int i = 0; i < getter.length; i++) {
				setter[i].invoke(fNew,
						(double) getter[i].invoke(this) * factor);
			}

			fNew.setName(this.getName());
			if (this.getPrice() != null) {
				fNew.setPrice(this.getPrice()*factor);
			}
			return fNew;

		} catch (IllegalArgumentException | InvocationTargetException
				| IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	public abstract double getPantothenicAcid();

	public abstract double getFatPolyUnsaturated();

	public abstract double getFatMonoUnsaturated();

	public abstract double getFatSaturated();

	public default IFood gram(double gram){
		return this.mult(gram / this.getWeight());
	}

	/**
	 * 
	 * 1 EL -> 10-15g
	 * @param i
	 * @return
	 */
	public default IFood el(int i){
		return this.gram(i*12);
	}
	
	
	public default Food free(){
		Food f = (Food) this.mult(1.0);
	    f.setPrice(0.0);
	    return f;
	}
	

}
