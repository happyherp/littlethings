package de.carlos.simplexFood.scraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class RezepteEu {
	
	private static final File JSONFILE = new File("data/eu5.json");

	List<String> urlsToSkip=Arrays.asList(new String[]{"http://rezepte.eu5.org/Auflauf/auflauf_r55.html"});

	String[] templates = new String[] { 
			"http://rezepte.eu5.org/Arabisch-Marokko/arabisch-maroko_r%d.html",
			"http://rezepte.eu5.org/Auflauf/auflauf_r%d.html",
			"http://rezepte.eu5.org/Beilagen/beilagen_r%d.html", "http://rezepte.eu5.org/Brot/brot_r%d.html",
			"http://rezepte.eu5.org/Curry/curry_r%d.html", "http://rezepte.eu5.org/Dessert/dessert_r%d.html",
			"http://rezepte.eu5.org/Diaet/diaet_r%d.html", "http://rezepte.eu5.org/Eier/eier_r%d.html",
			"http://rezepte.eu5.org/Eintopf/eintopf_r%d.html", "http://rezepte.eu5.org/Eis/eis_r%d.html",
			"http://rezepte.eu5.org/Ente/ente_r%d.html", "http://rezepte.eu5.org/Exotisch/exotisch_r%d.html",
			"http://rezepte.eu5.org/Fisch/fisch_r%d.html", "http://rezepte.eu5.org/Fleisch/fleisch_r%d.html",
			"http://rezepte.eu5.org/Gans/gans_r%d.html",
			"http://rezepte.eu5.org/Hackfleisch/hackfleisch_r%d.html",
			"http://rezepte.eu5.org/Huhn/huhn_r%d.html", "http://rezepte.eu5.org/Kaese/kaese_r%d.html",
			"http://rezepte.eu5.org/Kalb/kalb_r%d.html", "http://rezepte.eu5.org/Kaninchen/kaninchen_r%d.html",
			"http://rezepte.eu5.org/Kartoffel/kartoffel_r%d.html", "http://rezepte.eu5.org/Lamm/lamm_r%d.html",
			"http://rezepte.eu5.org/Nudeln/nudeln_r%d.html", 
			"http://rezepte.eu5.org/Suppen/suppen_r%d.html", };
	
	public static void main(String[] args) {
		RezepteEu rezepteEu = new RezepteEu();
		rezepteEu.run();
	}

	public void run() {
		
		try {


			List<Recipie> recipies = new ArrayList<>();

			for (String template : templates) {
				recipies.addAll(readRecipiesForTemplate(template));
			}

			Gson gson = new Gson();
			String asJson = gson.toJson(recipies);
			File outfile = JSONFILE;
			PrintWriter writer = new PrintWriter(outfile);
			writer.print(asJson);
			writer.close();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}

	private List<Recipie> readRecipiesForTemplate(String urlTemplate) throws IOException {
		List<Recipie> recipies = new ArrayList<>();
		int i = 0;
		while (true) {
			i++;
			String url = String.format(urlTemplate, i);
			if (urlsToSkip.contains(url)){
				continue;
			}
			try {
				Recipie recipie = readRecipie(url);
				recipies.add(recipie);
				//System.out.println("Read " + recipie.name + " " + url);
			} catch (NoRecipieException e) {
				if (i > 10) {
					System.out.println("Abbruch nach " + i + " rezeption mit template " + urlTemplate);
					break;
				} else {
					throw new RuntimeException("To early to quit on "+urlTemplate, e);
				}
			} catch (NoZutatenTagException e) {
				System.out.println("Kein Zutaten-Tag bei "+url);
			} catch (Exception e) {
				e.printStackTrace();
				//throw new RuntimeException("Unexpected Exception while doing "+url, e);
			}
		}
		return recipies;
	}

	private Recipie readRecipie(String url) throws IOException, NoRecipieException, NoZutatenTagException {
		Recipie recipie = new Recipie();
		recipie.url = url;
		Document doc = Jsoup.connect(url).followRedirects(false).get();
		if (doc.getElementsByTag("title").get(0).text().contains("302")) {
			throw new NoRecipieException();
		}

		if (doc.getElementsByClass("ingred").isEmpty()){
			throw new NoZutatenTagException();
		}
		Element zutatenH1 = doc.getElementsByClass("ingred").get(0);
		Elements ingredients = zutatenH1.nextElementSibling().getElementsByTag("tr");
		recipie.name = doc.getElementsByClass("title").get(0).text().trim();
		for (Element e : ingredients) {
			Elements cells = e.getElementsByTag("td");
			if (cells.size() == 4) {
				Ingredient ing = new Ingredient();
				ing.amount = parseAmount(cells.get(0).text());
				ing.unit = cells.get(1).text().trim();
				ing.name = cells.get(3).text().trim();
				recipie.ingredients.add(ing);
			}
		}
		return recipie;
	}

	public static Double parseAmount(String text) {
		try {
			text = text.trim().replace("\t", "");
			if (text.isEmpty()) {
				return null;
			}
			text = text.split("-")[0];
			if (text.contains("/")) {
				return Double.parseDouble(text.split("/")[0]) / Double.parseDouble(text.split("/")[1]);
			} else {
				return Double.parseDouble(text);
			}
		} catch (Exception e) {
			return null;
		}

	}
	
	public List<Recipie> readFromFile(){
		Gson gson = new Gson();
		TypeToken<List<Recipie>> type = new TypeToken<List<Recipie>>() {};
		List<Recipie> fromJson;
		try {
			fromJson = gson.fromJson(
					new JsonReader(new InputStreamReader(new FileInputStream(JSONFILE),"utf-8")), 
					type.getType());
			return fromJson;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

	public static class Recipie {

		List<Ingredient> ingredients = new ArrayList<>();

		String url;
		String name;

	}

	public static class Ingredient {
		Double amount;
		String unit;
		String name;
	}

}
