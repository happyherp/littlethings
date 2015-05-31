package de.carlos.simplexFood;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVParser;

public class SwissDB {

	public static final Map<String, Double> UNIT_FACTOR = new HashMap<>();
	static {
		UNIT_FACTOR.put("gram", 1.0);
		UNIT_FACTOR.put("milligram", 0.001);
		UNIT_FACTOR.put("microgram", 1.0E-6);
		UNIT_FACTOR.put("alpha-tocopherol equivalent", 1.0E-3);//Vitamin E mg
		UNIT_FACTOR.put("retinol equivalent", 1.0E-6); //Retinol in Mikrogramm
	}

	public List<Food> parseDB() {

		List<Food> foods = new ArrayList<Food>();
		BufferedReader r = null;
		try {
//			r = new BufferedReader(new InputStreamReader(this
//					.getClass().getResourceAsStream(
//							"Swiss Food Comp Data V5.0.csv")));
			r = new BufferedReader(new InputStreamReader(new FileInputStream(
						"src/main/java/de/carlos/simplexFood/Swiss Food Comp Data V5.0.csv")));
			
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
				food.name = getCellByName(cells, colNameToIndex, "name D");
				food.ballast = getGramsFromCell(cells, colNameToIndex,
						"dietary fibre, total");
				food.fett = getGramsFromCell(cells, colNameToIndex,
						"fat, total");
				food.kohlenhydrate = getGramsFromCell(cells, colNameToIndex,
						"charbohydrate, total");				
				food.protein = getGramsFromCell(cells, colNameToIndex,
								"protein");
				
				food.calcium = getGramsFromCell(cells, colNameToIndex,
						"calcium");
				food.eisen = getGramsFromCell(cells, colNameToIndex,
						"iron, total");
				food.iod = getGramsFromCell(cells, colNameToIndex,
						"iodide");				
				food.zink = getGramsFromCell(cells, colNameToIndex,
								"zinc");
				food.magnesium = getGramsFromCell(cells, colNameToIndex,
						"magnesium");		
				food.fluorid = getGramsFromCell(cells, colNameToIndex,
						"chlorid");	
				food.vitaminA = getGramsFromCell(cells, colNameToIndex,
						"vit A");	
				food.vitaminB1 = getGramsFromCell(cells, colNameToIndex,
						"B1");				
				food.vitaminB2 = getGramsFromCell(cells, colNameToIndex,
						"B2");
				food.vitaminB6 = getGramsFromCell(cells, colNameToIndex,
						"B6");
				food.vitaminB12 = getGramsFromCell(cells, colNameToIndex,
						"B12");
				food.vitaminC = getGramsFromCell(cells, colNameToIndex,
						"C");					
				food.vitaminD = getGramsFromCell(cells, colNameToIndex,
						"D");			
				food.vitaminE = getGramsFromCell(cells, colNameToIndex,
						"E");					
				food.niacin  = getGramsFromCell(cells, colNameToIndex,
						"niacine");	
				food.folat = getGramsFromCell(cells, colNameToIndex,
						"folate");

 
				String priceS = this.getCellByName(cells, colNameToIndex,
						"Preis");
				if (priceS.isEmpty()) {
					food.price = 1.5;//Assume a default price
				}else{
					food.price = readDouble(priceS);
				}
				String priceOverride = this.getCellByName(cells, colNameToIndex, "OverridePreis");
				if (!priceOverride.isEmpty()){
					food.price = readDouble(priceOverride);
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
		for (Food food : foods) {
			System.out.println(food.name + " ballast: " + food.ballast);
		}
	}

}
