package de.carlos.sugar;

import java.time.Instant;
import java.util.Calendar;

import de.carlos.sugar.unit.SugarFlow;

public class BaseSugarProduction {
	
	SugarFlow get(Instant time){
		
		Calendar clock = Calendar.getInstance();
		int hour = clock.get(Calendar.HOUR_OF_DAY);
		
		double val = 1.0;
		if (hour < 6){
			val = 2.0;
		}else if (hour < 12 ){
			val = 3.0;
		}
		
		
		return new SugarFlow(val);
	}
	

}
