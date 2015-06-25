package de.carlos.simplexFood;

import static de.carlos.simplexFood.food.Nutrient.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.optim.linear.Relationship;

import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class NutritionTarget {
	
    private static transient final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HibernateUtil.class);


	Map<Nutrient, Limit> target = new HashMap<>();
	
	
	public Limit get(Nutrient n){
		return this.target.get(n);
	}
	
	public void set(Nutrient n, Double min, Double max){
		this.target.put(n, new Limit(min, max));
	}
	
	
	public List<Restriction<IFood>> createRestrictions(){
		
		List<Restriction<IFood>> restrictions = new ArrayList<Restriction<IFood>>();
		
		for (Nutrient n: this.target.keySet()){
			Limit l = this.target.get(n);
			if (l.min != null && l.max != null && l.min == l.max){
				restrictions.add(new Restriction<IFood>(f->f.getNutrient(n), Relationship.EQ, l.min));
			}else{
				if (l.min != null){
					restrictions.add(new Restriction<IFood>(f->f.getNutrient(n), Relationship.GEQ, l.min));
				}
				if (l.max != null){
					restrictions.add(new Restriction<IFood>(f->f.getNutrient(n), Relationship.LEQ, l.max));
				}
			}		
		}
		
		return restrictions;
	}
	
	
	/**
	 * Subtract the nutrients of the given food from this target.
	 * 
	 * @param food
	 */
	public NutritionTarget subtract(IFood food){
		
		
		NutritionTarget subtracted = new NutritionTarget();
		for (Nutrient n: this.target.keySet()){
			subtracted.target.put(n, this.target.get(n).subtract(food.getNutrient(n)));
		}
		return subtracted;
	}
	
	private static final double TOLERANCE = 0.01;
	
	public boolean matches(IFood food){
		
		
		boolean match = true;
		
		for (Nutrient n : this.target.keySet()){
			Limit l = this.target.get(n);
			
			if (l.min != null){
				match = match && food.getNutrient(n) * (1.0+TOLERANCE) >= l.min;
			}
			if (l.max != null){
				match = match && food.getNutrient(n) * (1.0-TOLERANCE)<= l.max;
			}			
			
			if (!match){
				LOGGER.info("Did not match constraint on "+n);
			}
			
		}
		
		
		return match;
		
	}
	
	
	
	public void print(){
		System.out.println("Target requirements. ");
		for (Nutrient n : this.target.keySet()){
			Limit l = this.target.get(n);
			System.out.println(String.format("%20s min: %10.3eg max: %10eg", n, l.min, l.max));
		}
	}
	
	

	public class Limit{
		
		Double min;
		Double max;
		
		
		public Limit(Double min){
			this.min = min;
		}
		
		public Limit subtract(Double nutrient) {
			Limit l = new Limit(this.min == null?null:this.min - nutrient, 
					            this.max == null?null:this.max - nutrient );
			if (l.min != null && l.min <=0){
				l.min = null;
			}		
			
			if (l.max != null && l.max <= 0){
				throw new RuntimeException("Limit below zero.");
			}
			
			return l;
		}

		public Limit(Double min, Double max){
			this.min = min;
			this.max = max;
		}

		
	}
	
	public static NutritionTarget dailyMale(){
		
		NutritionTarget target = new NutritionTarget();
		

		// Basic Elements
		target.set(Kohlenhydrate, 340.0, 340.0);		
		//Braucht man stärke?
		target.set(Nutrient.Starch, 100.0, null);
		//(Fett, 80.0
		target.set(FatSaturated, 30.0, 30.);
		target.set(FatMonoUnsaturated, 29.0, 29.0);
		target.set(FatPolyUnsaturated, 21.0, 21.0);		
		target.set(Protein, 60.0, 60.0);
		target.set(Ballast, 25.0, 25.0);

		// Spurenelemente
		target.set(Calcium, 1.0, null);
		target.set(Eisen, 10.0E-3, 45.0E-3);
		target.set(Iod, 200.0E-6, 500.0E-6);
		target.set(Fluorid, 3.8E-3, null);
		target.set(Magnesium, 350.0E-3, null);
		target.set(Zink, 10.0E-3, 30.0E-3);

		// Vitamine
		target.set(VitaminA, 1.0E-3, 3.0E-3);
		target.set(BetaCarotene, 2.0E-3, 10.0E-3);
		target.set(VitaminB1, 1.2E-3, null);
		target.set(VitaminB2, 1.4E-3, null);
		target.set(VitaminB6, 1.5E-3, null);
		target.set(VitaminB12, 3.0E-6, null);
		target.set(VitaminC, 100.0E-3, null);
		target.set(VitaminD, 5E-6, null);
		target.set(VitaminE, 14E-3, 300E-3);
		target.set(VitaminK, 80.0E-6, null);
		target.set(Niacin, 16.0E-3, 32E-3);
		target.set(Folat, 400.0E-6, null);	
		target.set(PantothenicAcid, 6.0E-3, null);	
		target.set(Sodium, 500.0E-3, null);	
		target.set(Chloride, 2.0, null);	
		
		
		
		return target;
		
		
	}

}
