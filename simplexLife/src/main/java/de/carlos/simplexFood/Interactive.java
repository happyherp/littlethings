package de.carlos.simplexFood;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.derby.tools.sysinfo;

import de.carlos.simplexOO.SimplexOO.Restriction;

public class Interactive {

	public void start(List<? extends IFood> available) {

		try {
			List<Restriction<IFood>> extraRestrictions = new ArrayList<>();
			BufferedReader lineIn = new BufferedReader(new InputStreamReader(
					System.in));

			while (true) {
				List<IFood> result = new FoodOptimize().optimize(available,
						extraRestrictions);
				result.sort((a,b)->(int) (b.getWeight() - a.getWeight()));
				FoodOptimize.printSummary(result);

				System.out.println("Choose food to be removed:");
				IFood toRemove = result
						.get(Integer.parseInt(lineIn.readLine()) - 1);
				available.remove(available.stream()
						.filter(f -> f.getName().equals(toRemove.getName()))
						.findFirst().get());
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {

		new Interactive().start(new SwissDB().parseDB());

	}

}
