package de.carlos.simplexLife;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MyTest {
    
    
    @Test
    public void test(){
	
	Activity work = new Activity(60, -14.00, 420);
	work.name = "Work";
	Assert.assertEquals(7, work.getUtilityPerMinute(), 0.001);
	
	Activity skyVacation = new Activity(60*24*7, 1500);
	skyVacation.name = "Skying";
	skyVacation.setUtilityPerMinute(20);
	Assert.assertEquals(201600L, skyVacation.utility);
	
	
	Activity d3 = new Activity(60, 0);
	d3.name = "Diablo 3";
	d3.setUtilityPerMinute(10.0);

	
	
	Map<Activity, Double> result = new Optimizer().optimize(work, skyVacation, d3);
	
	Assert.assertTrue(result.containsKey(work));
	Assert.assertTrue(result.containsKey(skyVacation));
	
	double timeSpend = 0;
	for (Activity act : result.keySet()){
	    timeSpend += act.durationMin * result.get(act);
	}
	Assert.assertEquals(60*24*7, timeSpend, 0.1);
	
	Optimizer.printResult(result);

    }

}
