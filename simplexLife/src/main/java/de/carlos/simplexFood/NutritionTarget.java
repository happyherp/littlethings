package de.carlos.simplexFood;

import static de.carlos.simplexFood.food.Nutrient.Fiber;
import static de.carlos.simplexFood.food.Nutrient.BetaCarotene;
import static de.carlos.simplexFood.food.Nutrient.Calcium;
import static de.carlos.simplexFood.food.Nutrient.Calories;
import static de.carlos.simplexFood.food.Nutrient.Chloride;
import static de.carlos.simplexFood.food.Nutrient.Eisen;
import static de.carlos.simplexFood.food.Nutrient.FatMonoUnsaturated;
import static de.carlos.simplexFood.food.Nutrient.FatPolyUnsaturated;
import static de.carlos.simplexFood.food.Nutrient.FatSaturated;
import static de.carlos.simplexFood.food.Nutrient.FatTotal;
import static de.carlos.simplexFood.food.Nutrient.Fluorid;
import static de.carlos.simplexFood.food.Nutrient.Folat;
import static de.carlos.simplexFood.food.Nutrient.Iod;
import static de.carlos.simplexFood.food.Nutrient.Carbohydrates;
import static de.carlos.simplexFood.food.Nutrient.Magnesium;
import static de.carlos.simplexFood.food.Nutrient.Niacin;
import static de.carlos.simplexFood.food.Nutrient.PantothenicAcid;
import static de.carlos.simplexFood.food.Nutrient.Protein;
import static de.carlos.simplexFood.food.Nutrient.Sodium;
import static de.carlos.simplexFood.food.Nutrient.Starch;
import static de.carlos.simplexFood.food.Nutrient.VitaminA;
import static de.carlos.simplexFood.food.Nutrient.VitaminB1;
import static de.carlos.simplexFood.food.Nutrient.VitaminB12;
import static de.carlos.simplexFood.food.Nutrient.VitaminB2;
import static de.carlos.simplexFood.food.Nutrient.VitaminB6;
import static de.carlos.simplexFood.food.Nutrient.VitaminC;
import static de.carlos.simplexFood.food.Nutrient.VitaminD;
import static de.carlos.simplexFood.food.Nutrient.VitaminE;
import static de.carlos.simplexFood.food.Nutrient.VitaminK;
import static de.carlos.simplexFood.food.Nutrient.Zink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.optim.linear.Relationship;

import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Nutrient;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class NutritionTarget implements Cloneable{
	
    private static transient final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HibernateUtil.class);


	Map<Nutrient, Limit> target = new HashMap<>();
	
	
	public NutritionTarget(Map<Nutrient, Limit> target) {
		this.target = new HashMap<>(target);
	}

	public NutritionTarget() {
	}

	public Limit get(Nutrient n){
		return this.target.get(n);
	}
	
	public void set(Nutrient n, Double min, Double max){
		this.target.put(n, new Limit(min, max));
	}
	
	public void set(Nutrient n, Limit mult) {
		this.target.put(n, mult);
		
	}
	
	public Set<Nutrient> nutrients(){
		return target.keySet();
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
	

	
	@Override
	public NutritionTarget clone(){
		return new NutritionTarget(this.target);
	}

	public static class Limit{
		
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

		public Limit mult(double factor) {
			return new Limit(this.min==null?null:this.min*factor, this.max == null?null:this.max*factor);
		}

		public Double getMin() {
			return min;
		}

		public Double getMax() {
			return max;
		}
		
		
		
	}
	
	public static NutritionTarget dailyMale(){
		
		NutritionTarget target = new NutritionTarget();
		

		// Basic Elements
		target.set(Carbohydrates, 340.0, 340.0);		
		//Braucht man st�rke?
		target.set(Starch, 100.0, null);
		//(Fett, 80.0
		target.set(FatSaturated, 30.0, 30.);
		target.set(FatMonoUnsaturated, 29.0, 29.0);
		target.set(FatPolyUnsaturated, 21.0, 21.0);		
		target.set(Protein, 60.0, 60.0);
		target.set(Fiber, 25.0, 25.0);

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
	
	public static NutritionTarget dailyAlisherNontrain(){
		
		NutritionTarget target = new NutritionTarget();
		

		// Basic Elements
		target.set(Carbohydrates, 150.0, 200.0);		
		target.set(Starch, 100.0, null);
		target.set(FatTotal, 80.0, 100.0);
		target.set(FatSaturated, 30.0, null);
		target.set(FatMonoUnsaturated, 29.0, null);
		target.set(FatPolyUnsaturated, 21.0, null);		
		target.set(Protein, 150.0, 150.0);
		target.set(Fiber, 25.0, 25.0);

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


public static NutritionTarget anyCalories(){
		
		NutritionTarget target = new NutritionTarget();
		

		// Basic Elements
		target.set(Calories, 2800.0, 2800.0);
		target.set(Fiber, 25.0, 25.0);

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
