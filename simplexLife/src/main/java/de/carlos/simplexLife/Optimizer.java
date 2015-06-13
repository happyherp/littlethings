package de.carlos.simplexLife;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class Optimizer {
    
    private static final long  WEEK_MINUTES = 60*24*7;
    
    public Map<Activity, Double> optimize(Activity... activities) {
	return this.optimize(WEEK_MINUTES, activities);
    }
    
    public Map<Activity, Double> optimize(long timeM, Activity... activities){
	return this.optimize(timeM, new ArrayList<Alternative>(), activities);
    }
    
    public Map<Activity, Double> optimize(long timeM, List<Alternative> alternatives, Activity... activities){
	 Collection<LinearConstraint> cons =  new ArrayList<>();

	
	 double[] utilities = new double[activities.length];
	 double[] costs = new double[activities.length];
	 double[] durations = new   double[activities.length];
	 int i = 0;
	 for(Activity act: activities){
	     utilities[i] = (double) act.utility;
	     costs[i] = act.cost;
	     durations[i] = (double) act.durationMin;
	     
	     if (act.maximum != null){
		 double[] da = new double[activities.length];
		 da[i] = 1;
		 cons.add(new LinearConstraint(da, Relationship.LEQ, (double)act.maximum));
	     }

	     i++;
	 }
	 
	 cons.add(new LinearConstraint(costs, Relationship.LEQ, 0));
	 cons.add(new LinearConstraint(durations, Relationship.EQ, timeM));
	 
	 for (Alternative alt: alternatives){
	     double[] da = new double[activities.length];
	     for (Activity act : alt.activities){
		 da[ArrayUtils.indexOf(activities, act)] = 1;
	     }
             cons.add(new LinearConstraint(da, Relationship.EQ, 1.0));
	 }

	
	 LinearObjectiveFunction objective = new LinearObjectiveFunction(utilities, 0);
	 	
	 PointValuePair pvp = new SimplexSolver()
	      .optimize(objective, GoalType.MAXIMIZE, new NonNegativeConstraint(true),
	    		  new LinearConstraintSet(cons));
			 
			 	 
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
		    "%4.1f times %30s. Time %4dm Utility: %5d",
		    times, act.name , (long)(times*act.durationMin), (long) (times* act.utility)));
	}
	System.out.println("Total Utility: "+totalUtil);
    }




}
