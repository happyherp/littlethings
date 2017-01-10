package de.carlos.simplexOO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import de.carlos.simplexFood.food.IFood;
import de.carlos.simplexOO.SimplexOO.Restriction;

public class SimplexOO<T> {
	
    /** Default amount of error to accept in floating point comparisons (as ulps). */
    public static final int DEFAULT_ULPS = 10;

    /** Default cut-off value. */
    public static final double DEFAULT_CUT_OFF = 1e-12;

    /** Default amount of error to accept for algorithm convergence. */
    public static final double DEFAULT_EPSILON = 1.0e-6;
    
    
    
	private double epsilon;
	private int maxUlps;
	private double cutOff;

	public SimplexOO(final double epsilon, final int maxUlps, final double cutOff) {
        this.epsilon = epsilon;
        this.maxUlps = maxUlps;
        this.cutOff = cutOff;
    
	}
	
	public SimplexOO(){
		this(DEFAULT_EPSILON, DEFAULT_ULPS, DEFAULT_CUT_OFF);
	}
 
	public Map<T, Double> solve(Collection<T> elements,
			Collection<Restriction<T>> restrictions,
			ToDoubleFunction<T> goalFunction, GoalType goaltype) {

		List<T> elementsL = new ArrayList<>(elements);

		LinearObjectiveFunction objective = new LinearObjectiveFunction(
				elementsL.stream().mapToDouble(goalFunction).toArray(), 0);

		List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		for (Restriction<T> restr : restrictions) {
			constraints.add(
					new LinearConstraint(elementsL.stream()
							.mapToDouble(restr.accessor).toArray(),
							restr.rel, 
							restr.value));
		}

		PointValuePair pvp = new SimplexSolver(this.epsilon, this.maxUlps, this.cutOff)
		   .optimize(objective,
				new LinearConstraintSet(constraints),
				new NonNegativeConstraint(true));

		Map<T, Double> result = new HashMap<>();
		for (int i = 0; i < pvp.getKey().length; i++) {
			double factor = pvp.getKey()[i];
			if (factor > 0) {
				result.put(elementsL.get(i), factor);
			}
		}

		return result;

	}

	public static class Restriction<T> {

		private ToDoubleFunction<T> accessor;

		private Relationship rel;
		
		private double value;

		public Restriction(ToDoubleFunction<T> accessor, Relationship rel, double val) {
			super();
			this.accessor = accessor;
			this.rel = rel;
			this.value = val;
		}

	}
	
	public static <Q> Restriction<Q> atLeast(double amount, Q obj){
		return new Restriction<Q>(e->e.equals(obj)?1:0, Relationship.GEQ, amount);
	}

	
}
