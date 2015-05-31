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
    
    
    @Test
    public void testMeWeek(){
	
	List<Alternative> alts = new ArrayList<>();

	
	Activity work1 = new Activity(60, - 25 * 0.85 * 1);
	work1.name = "Work1";
	work1.maximum = 7L;
	work1.setUtilityPerMinute(10);
	
	Activity work2 = new Activity(60, - 25 * 0.85 * 0.75);
	work2.name = "Work2";
	work2.maximum = 20-7L;
	work2.setUtilityPerMinute(9);
	
	Activity work3 = new Activity(60, - 25 * 0.85 * 0.65);
	work3.name = "work3";
	work3.maximum = 40-20-7L;
	work3.setUtilityPerMinute(7);
	
	Activity work4 = new Activity(60, - 25 * 0.85 * 0.58);
	work4.name = "work4";
	work4.setUtilityPerMinute(5);
	
	Activity skyVacation = new Activity(60*24*7, 1500);
	skyVacation.name = "Skying";
	skyVacation.setUtilityPerMinute(20);
	
	
	Activity cleanHouseSelf = new Activity(180, 10);
	cleanHouseSelf.name ="clean House";
	cleanHouseSelf.setUtilityPerMinute(4);
	
	Activity houseHelp = new Activity(20, 3*9, 0);
	houseHelp.name = "Househelp";
	
	alts.add(new Alternative(cleanHouseSelf, houseHelp));
	
	Activity eatHome = new Activity(180, 10);
	eatHome.name ="EatHomee";
	eatHome.setUtilityPerMinute(4);
	

	
	alts.add(new Alternative(eatHome, null));

	
	
	long availableTime = 60*24*7;
	availableTime-= 7*8*24;//Sleep.
	
	Map<Activity, Double> result = new Optimizer().optimize(availableTime);

	
    }


}
