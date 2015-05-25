package de.carlos.simplexLife;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.SimplexSolver;

public class Optimizer {
    
    private static final long  WEEK_MINUTES = 60*24*7;
    
    public Map<Activity, Double> optimize(Activity... activities){
	
	
	 double[] utilities = new double[activities.length];
	 double[] costs = new double[activities.length];
	 double[] durations = new double[activities.length];
	 int i = 0;
	 for(Activity act: activities){
	     utilities[i] = (double) act.utility;
	     costs[i] = act.cost;
	     durations[i] = (double) act.durationMin;
	     i++;
	 }
	
	
	 LinearObjectiveFunction objective = new LinearObjectiveFunction(utilities, 0);
	 
	 Collection<LinearConstraint> cons =  new ArrayList<>();
	 cons.add(new LinearConstraint(costs, Relationship.LEQ, 0));
	 cons.add(new LinearConstraint(durations, Relationship.EQ, WEEK_MINUTES));
	 
	
	 PointValuePair pvp = new SimplexSolver().optimize(objective, cons, GoalType.MAXIMIZE, true);
	 
	 Map<Activity, Double> result = new HashMap<>();
	 i = 0;
	 for(Activity act: activities){
	     result.put(act, pvp.getKey()[i]);
	     i++;
	 }
	 
	
	return result;
    }
    
    
    public static void printResult(Map<Activity, Double> result){
	int totalUtil = 0;
	for (Activity act : result.keySet()){
	    double times = result.get(act);
	    totalUtil += act.utility * times;
	    System.out.println(String.format(
		    "%4.1f times %10s. Time %4dm ",
		    times, act.name , (long)(times*act.durationMin)));
	}
	System.out.println("Total Utility: "+totalUtil);
    }

}
