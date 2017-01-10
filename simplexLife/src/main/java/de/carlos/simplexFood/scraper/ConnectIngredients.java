package de.carlos.simplexFood.scraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Recipe;
import de.carlos.simplexFood.scraper.RezepteEu.Ingredient;
import de.carlos.simplexFood.scraper.RezepteEu.Recipie;
import de.carlos.simplexFood.swissDB.SwissDB;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.interfaces.StringDistance;

public class ConnectIngredients {
	
	private static final String MAPPING = "/eu50ToSwiss.csv";

	public Recipe convert(Recipie recipie, List<IFood> ingredients ){
		Recipe meal = new Recipe();
		meal.setName(recipie.name);
		meal.setSource(recipie.url);
		
		recipie.ingredients.stream()
			.map(i -> this.convertIngredient(i,ingredients))
			.forEach(meal::addIngredient);
		
		return meal;
	}
	
	public IFood convertIngredient(Ingredient ingredient, List<? extends IFood> ingredientList){
		
		StringDistance distanceCalc = new Cosine(3);
		IFood bestMatch = ingredientList.stream()
				.min(Comparator.comparingDouble(
						i->distanceCalc.distance(i.getName(), ingredient.name)))
				.get();
		
		IFood result = applyAmount(ingredient, bestMatch);
		return result;
	}

	private IFood applyAmount(Ingredient ingredient, IFood bestMatch) {
		IFood result = null;
		String unit = ingredient.unit
				.toLowerCase()
				.replaceAll("[^\\p{Alpha}]+",""); 
		switch (unit) {
		case "g":
		case "ml":
		case "gramm":
			result = bestMatch.gram(ingredient.amount);
			break;
		case "kg":
		case "ltr":
			result = bestMatch.gram(ingredient.amount*1000);
			break;
		case "essl":
			result = bestMatch.el(ingredient.amount.intValue());
			break;
		case "teel":
			result = bestMatch.tl(ingredient.amount.intValue());
			break;	
		case "prise":
			result = bestMatch.gram(0.2);
		default:
			throw new UnknownUnitException(ingredient.unit);
		}
		return result;
	}
	
	
	
	public static Map<String, String> readCSV(){
		HashMap<String, String> nameToName = new HashMap<String,String>();
		
		try {
			CSVReader csvReader = new CSVReader(new FileReader("src/main/resources" + MAPPING), ';','"');
			csvReader.readAll().forEach(array->nameToName.put(array[0], array[1]));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return nameToName;
	}
	
	public static void main(String[] args){

		try {
			Map<String, String> existing = readCSV();
			
			File outfile = new File("src/main/resources" + MAPPING);
			CSVWriter csvwriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outfile, true), "utf-8"), ';', '\"');

			List<Food> swissIngredients = new SwissDB().parseDB();
			List<Recipie> eu5Recipies = new RezepteEu().readFromFile();

			BufferedReader lineIn = new BufferedReader(new InputStreamReader(System.in));

			StringDistance distanceCalc = new Cosine(3);
			eu5Recipies.stream().flatMap(r -> r.ingredients.stream()).forEach(ingredientEu50 -> {

				String name = RezepteEu.cleanName(ingredientEu50.name);
				try {
					if (!existing.containsKey(name)) {

						IFood selected = selectFromList(swissIngredients, lineIn, distanceCalc, name);
						if (selected != null){
							csvwriter.writeNext(new String[]{name, selected.getName()});
							csvwriter.flush();
							existing.put( name, selected.getName());
						}
						System.out.println("-------------------------------------------------------");

					}
				} catch (Exception e) {
					e.printStackTrace(System.out);
					System.out.flush();
				}
			});
			csvwriter.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static IFood selectFromList(List<Food> swissIngredients,
			BufferedReader lineIn, StringDistance distanceCalc, String ingredientName) throws IOException {
		List<IFood> bestMatches = swissIngredients.stream()
				.sorted(Comparator.comparingDouble(ingredientSwiss -> distanceCalc
						.distance(ingredientSwiss.getName(), ingredientName)))
				.limit(100).collect(Collectors.toList());
		for (int i = bestMatches.size() - 1; i >= 0; i--) {
			System.out.println((i + 1) + " " + bestMatches.get(i).getName() + " score: "
					+ distanceCalc.distance(bestMatches.get(i).getName(), ingredientName));
		}
		System.out.println("Find Match for: " + ingredientName);
		System.out.print("Your selection:");

		String input = lineIn.readLine();

		if (input.matches("[0-9]+")) {
			return bestMatches.get(Integer.parseInt(input) - 1);

		} else if (input.equals("x")){
			System.out.println("Skipping this one.");
			return null;
		}else{
			System.out.println("Switching");
			return selectFromList( swissIngredients, lineIn, distanceCalc, input);
		}
	}

}
