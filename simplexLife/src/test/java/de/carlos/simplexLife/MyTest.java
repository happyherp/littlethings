package de.carlos.simplexLife;

import java.util.ArrayList;
import java.util.List;
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
    
    
    @Test
    public void testMinimum(){
	
	Activity work = new Activity(60, -14.00, 420);
	work.name = "Work";
	work.maximum = 30L;
	
	Activity skyVacation = new Activity(60*24*7, 1500);
	skyVacation.name = "Skying";
	skyVacation.setUtilityPerMinute(20);
	
	Activity idle = new Activity(60, 0);
	idle.name = "Idle";
	idle.setUtilityPerMinute(5);

	
	
	Map<Activity, Double> result = new Optimizer().optimize(work, skyVacation, idle);
	
	Assert.assertEquals(30.0,result.get(work), 0.001 );
	
	Optimizer.printResult(result);

    }
    
    @Test
    public void testSmall(){
	
	Activity work = new Activity(60, -14.00, 420);
	work.name = "Work";
	
	Activity skyVacation = new Activity(60*24*7, 1500);
	skyVacation.name = "Skying";
	skyVacation.setUtilityPerMinute(20);
	
	Activity idle = new Activity(60, 0);
	idle.name = "Idle";
	idle.setUtilityPerMinute(5);

	long dayM = 60*24;
	
	Map<Activity, Double> result = new Optimizer().optimize(dayM,work, skyVacation, idle);
	
	double timeSpend = 0;
	for (Activity act : result.keySet()){
	    timeSpend += act.durationMin * result.get(act);
	}
	Assert.assertEquals(dayM, timeSpend, 0.1);
		
	Optimizer.printResult(result);
    }
    
    
    @Test
    public void testHighwaySpeed(){
	
	Activity work = new Activity(60, -10.00, 420);
	work.name = "Work";
	
	Activity idle = new Activity(60, 0);
	idle.name = "Play";
	idle.setUtilityPerMinute(10);
	
	Activity drive100 = new Activity(60, 5.00);
	drive100.setUtilityPerMinute(3);
	drive100.name = "Driving 100kmh";
	
	
	Activity drive200 = new Activity(30, 20.00);
	drive200.setUtilityPerMinute(4);
	drive200.name = "Driving 200kmh";
	
	List<Alternative> alts = new ArrayList<>();
	alts.add(new Alternative(drive100, drive200));
	
	Map<Activity, Double> result = new Optimizer().optimize(60*24, alts, work, drive100, drive200, idle);
	
	Optimizer.printResult(result);

    }


}
