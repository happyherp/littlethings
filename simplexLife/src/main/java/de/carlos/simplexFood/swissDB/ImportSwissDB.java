package de.carlos.simplexFood.swissDB;

import java.util.List;

import de.carlos.simplexFood.HibernateUtil;
import de.carlos.simplexFood.food.Food;

public class ImportSwissDB {
	
	public static void main(String[] args){
		
		List<Food> foods = new SwissDB().parseDB();
		
		
		HibernateUtil.beginTransaction();
		
		for (Food food: foods){
			
			HibernateUtil.getSession().save(food);
			
			
			
		}
		
		HibernateUtil.getSession().getTransaction().commit();
		
		
	}

}
