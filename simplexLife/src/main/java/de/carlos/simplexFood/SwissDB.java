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
				food.setName(getCellByName(cells, colNameToIndex, "name D"));
				food.setBallast(getGramsFromCell(cells, colNameToIndex,
						"dietary fibre, total"));
				food.setFett(getGramsFromCell(cells, colNameToIndex,
						"fat, total"));
				food.setKohlenhydrate(getGramsFromCell(cells, colNameToIndex,
						"charbohydrate, total"));				
				food.setProtein(getGramsFromCell(cells, colNameToIndex,
								"protein"));
				
				food.setCalcium(getGramsFromCell(cells, colNameToIndex,
						"calcium"));
				food.setEisen(getGramsFromCell(cells, colNameToIndex,
						"iron, total"));
				food.setIod(getGramsFromCell(cells, colNameToIndex,
						"iodide"));				
				food.setZink(getGramsFromCell(cells, colNameToIndex,
								"zinc"));
				food.setMagnesium(getGramsFromCell(cells, colNameToIndex,
						"magnesium"));		
				food.setFluorid(getGramsFromCell(cells, colNameToIndex,
						"chlorid"));	
				food.setVitaminA(getGramsFromCell(cells, colNameToIndex,
						"vit A"));	
				food.setVitaminB1(getGramsFromCell(cells, colNameToIndex,
						"B1"));				
				food.setVitaminB2(getGramsFromCell(cells, colNameToIndex,
						"B2"));
				food.setVitaminB6(getGramsFromCell(cells, colNameToIndex,
						"B6"));
				food.setVitaminB12(getGramsFromCell(cells, colNameToIndex,
						"B12"));
				food.setVitaminC(getGramsFromCell(cells, colNameToIndex,
						"C"));					
				food.setVitaminD(getGramsFromCell(cells, colNameToIndex,
						"D"));			
				food.setVitaminE(getGramsFromCell(cells, colNameToIndex,
						"E"));					
				food.setNiacin(getGramsFromCell(cells, colNameToIndex,
						"niacine"));	
				food.setFolat(getGramsFromCell(cells, colNameToIndex,
						"folate"));
				food.setWeight(100.0);

 
				String priceS = this.getCellByName(cells, colNameToIndex,
						"Preis");
				if (priceS.isEmpty()) {
					food.setPrice(1.5);//Assume a default price
				}else{
					food.setPrice(readDouble(priceS));
				}
				String priceOverride = this.getCellByName(cells, colNameToIndex, "OverridePreis");
				if (!priceOverride.isEmpty()){
					food.setPrice(readDouble(priceOverride));
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
		for (IFood food : foods) {
			System.out.println(food.getName() + " ballast: " + food.getBallast());
		}
	}

}
