package de.carlos.simplexFood.scraper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import de.carlos.simplexFood.food.Food;
import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexFood.food.Recipe;
import de.carlos.simplexFood.scraper.RezepteEu.Ingredient;
import de.carlos.simplexFood.scraper.RezepteEu.Recipie;
import de.carlos.simplexFood.swissDB.SwissDB;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.interfaces.StringDistance;

public class ConnectIngredients {
	
	private static final String MAPPING = "/eu50ToSwiss.properties";

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
	
	public static void main(String[] args){
		
		
		try {
			Properties props = new Properties();
			props.load(ConnectIngredients.class.getResourceAsStream(MAPPING));
			File outfile = new File("src/main/resources"+MAPPING);
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outfile, true), "utf-8"));
			
			List<Food> swissIngredients = new SwissDB().parseDB();			
			List<Recipie> eu5Recipies = new RezepteEu().readFromFile();
			
			BufferedReader lineIn = new BufferedReader(new InputStreamReader(
					System.in));
			
			StringDistance distanceCalc = new Cosine(3);
			eu5Recipies.stream()
			.flatMap(r->r.ingredients.stream())
			.forEach(ingredientEu50->{
				
				
				if (!props.containsKey(ingredientEu50.name)){
				
					System.out.println("Find Match for: "+ingredientEu50.name);
					List<IFood> bestMatches = swissIngredients.stream()
							.sorted(Comparator.comparingDouble(
									ingredientSwiss->distanceCalc.distance(ingredientSwiss.getName(), ingredientEu50.name)))
							.limit(10)
							.collect(Collectors.toList());
					for(int i = 0;i<bestMatches.size();i++){
						System.out.println((i+1) +" "+bestMatches.get(i).getName()+" score: "+distanceCalc.distance(bestMatches.get(i).getName(), ingredientEu50.name));
					}
					System.out.print("Your selection:" );
					try{
						IFood selected = bestMatches.get(Integer.parseInt(lineIn.readLine())-1);
						writer.println(ingredientEu50.name+"="+selected.getName());
						props.put(ingredientEu50.name, selected.getName());
						writer.flush();
					}catch(Exception e){
						e.printStackTrace(System.out);
						System.out.flush();
					}
					System.out.println("-------------------------------------------------------");
				}				
			});
			writer.close();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
