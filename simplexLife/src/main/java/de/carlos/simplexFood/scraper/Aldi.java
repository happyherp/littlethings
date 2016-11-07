package de.carlos.simplexFood.scraper;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Aldi {
	
	
	public static void main(String[] args){
		try {
			Document doc = Jsoup.connect("http://www.aldi-nord.de/aldi_rezept_haehnchen_rollbraten_mit_ananas_reis_und_moehren_salat_1770_413.html").get();
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
