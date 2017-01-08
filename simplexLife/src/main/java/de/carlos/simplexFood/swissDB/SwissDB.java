package de.carlos.simplexFood.swissDB;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Nutrient;

import static de.carlos.simplexFood.food.Nutrient.*;
import au.com.bytecode.opencsv.CSVParser;

public class SwissDB {
	
	public static final double DEFAULT_PRICE = 1.00;

	public static final Map<String, Double> UNIT_FACTOR = new HashMap<>();
	static {
		UNIT_FACTOR.put("gram", 1.0);
		UNIT_FACTOR.put("milligram", 0.001);
		UNIT_FACTOR.put("microgram", 1.0E-6);
		UNIT_FACTOR.put("alpha-tocopherol equivalent", 1.0E-3);//Vitamin E mg
		UNIT_FACTOR.put("retinol equivalent", 1.0E-6); //Retinol in Mikrogramm
		UNIT_FACTOR.put("beta-carotene equivalent",  1.0E-6);
	}
	
	static final Map<String, Nutrient> colToNutrient = new HashMap<>();
	static{
		colToNutrient.put("dietary fibre, total",Fiber);
		
		colToNutrient.put("fat, total", FatTotal);
		colToNutrient.put("fatty acids, total saturated", FatSaturated);
		colToNutrient.put("fatty acids, total mono unsaturated", FatMonoUnsaturated);
		colToNutrient.put("fatty acids, total poly unsaturated", FatPolyUnsaturated);
		
		colToNutrient.put("charbohydrate, total", Carbohydrates);	
		colToNutrient.put("sugar, total", Sugar);	
		colToNutrient.put("starch, total", Starch);		
		colToNutrient.put("alcohol", Alcohol);
		
		
		colToNutrient.put("protein", Protein);
		
		colToNutrient.put("calcium", Calcium);
		colToNutrient.put("iron, total", Eisen);
		colToNutrient.put("iodide", Iod);				
		colToNutrient.put("zinc", Zink);
		colToNutrient.put("magnesium", Magnesium);		
		colToNutrient.put("sodium", Sodium);	
		colToNutrient.put("niacine", Niacin);	
		colToNutrient.put("folate", Folat);
		colToNutrient.put("potassium", Potassium);					
		colToNutrient.put("chlorid", Chloride);
		
		colToNutrient.put("vit A", VitaminA);	
		colToNutrient.put("beta-carotene equivalents", BetaCarotene);					
		colToNutrient.put("B1", VitaminB1);				
		colToNutrient.put("B2", VitaminB2);
		colToNutrient.put("B6", VitaminB6);
		colToNutrient.put("B12", VitaminB12);
		colToNutrient.put("C", VitaminC);					
		colToNutrient.put("D", VitaminD);			
		colToNutrient.put("E", VitaminE);			
		colToNutrient.put("VitaminK", VitaminK);						
		colToNutrient.put("pantothenic acid", PantothenicAcid);	
		
	}

	public List<Food> parseDB() {

		List<Food> foods = new ArrayList<>();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new InputStreamReader(this
					.getClass().getResourceAsStream(
							"/Swiss Food Comp Data V5.0_2.csv"), "utf-8"));
//			r = new BufferedReader(new InputStreamReader(new FileInputStream(
//						"src/main/java/de/carlos/simplexFood/swissDB/Swiss Food Comp Data V5.0_2.csv")));
			
			CSVParser parser = new CSVParser(',', '"');

			String[] header = parser.parseLine(r.readLine());
			Map<String, Integer> colNameToIndex = new HashMap<>();
			for (int i = 0; i < header.length; i++) {
				colNameToIndex.put(header[i], i);
			}
			System.out.println("Columns" + colNameToIndex);

			int i = 0;
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				String[] cells = parser.parseLine(line);

				if (cells.length != header.length) {
					throw new RuntimeException("Unexpected line length");
				}

				Food food = new Food();
				food.setName(getCellByName(cells, colNameToIndex, "name D"));
				
				for (String col: colToNutrient.keySet()){
					food.setNutrient(colToNutrient.get(col), getGramsFromCell(cells, colNameToIndex, col));
				}				

				food.setWeight(100.0);

				String priceS = this.getCellByName(cells, colNameToIndex,
						"Preis").trim();
				if (priceS.isEmpty()) {
					food.setPrice(DEFAULT_PRICE);//Assume a default price
				}else{
					food.setPrice(readDouble(priceS));
				}
				String priceOverride = this.getCellByName(cells, colNameToIndex, "OverridePreis");
				if (!priceOverride.isEmpty()){
					food.setPrice(readDouble(priceOverride));
				}
				
				
				String ing = this.getCellByName(cells, colNameToIndex, "Edible");
				if (!ing.isEmpty()){
					if (ing.toLowerCase().equals("x")){
						food.setEdible(true);
					}else{
						throw new RuntimeException("Unexpected value for Noningredient in "+food.getName());
					}
				}else{
					food.setEdible(false);
				}
				

				foods.add(food);
				i++;
			}
			System.out.println("lines: " + i);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (r != null) {
					r.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return foods;

	}

	private double getGramsFromCell(String[] cells,
			Map<String, Integer> colNameToIndex, String string) {
		Integer index = colNameToIndex.get(string);
		String val = cells[index];
		if (val.isEmpty()) {
			return 0.0;
		}
		String unitName = cells[index + 1];
		if (!UNIT_FACTOR.containsKey(unitName)){
			throw new RuntimeException("Unknown Unit "+unitName);
		}
		double factor = UNIT_FACTOR.get(unitName);

		return factor * readDouble(val);
	}

	private double readDouble(String s) {
		return Double.parseDouble(s.replace(",", "."));
	}

	private String getCellByName(String[] cells,
			Map<String, Integer> colNameToIndex, String string) {

		Integer index = colNameToIndex.get(string);
		if (index == null) {
			throw new RuntimeException("No column named " + string);
		}
		return cells[index];
	}

	public static void main(String[] args) {
		List<Food> foods = new SwissDB().parseDB();
		for (IFood food : foods) {
			System.out.println(food.getName() + " ballast: " + food.getNutrient(Fiber));
		}
	}

}
