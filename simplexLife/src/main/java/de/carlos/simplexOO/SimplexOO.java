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

public class SimplexOO<T> {

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

		PointValuePair pvp = new SimplexSolver().optimize(objective,
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

}
