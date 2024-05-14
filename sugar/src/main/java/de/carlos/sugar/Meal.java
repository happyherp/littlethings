package de.carlos.sugar;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

import de.carlos.sugar.unit.Carbs;
import de.carlos.sugar.unit.GlycemicIndex;
import de.carlos.sugar.unit.SugarFlow;
import lombok.Data;

public @Data class Meal {

	private final Instant consumedAt;
	private final Carbs carbs;
	private final TemporalAmount timeToMax;

	public SugarFlow get(Instant time) {

		// Current model is linear
		//TODO: use this: f(x) = b*(x+a)^4+ c*(x+a)^2+d
		double gramsPerHourSq = carbs.getGrams() / (timeToMax.get(ChronoUnit.SECONDS) * 60 * 60);

		Instant highestEffectAt = consumedAt.plus(this.timeToMax);

		double hoursSinceConsumption = ((double) time.getEpochSecond() - consumedAt.getEpochSecond()) / (60 * 60);

		if (time.isBefore(consumedAt)) {
			return new SugarFlow(0);
		} else if (time.isBefore(highestEffectAt)) {
			return new SugarFlow(gramsPerHourSq * hoursSinceConsumption);
		} else if (time.isBefore(getEndOfEffect())) {
			return new 
		}

	}

	Instant getEndOfEffect() {
		return consumedAt.plus(timeToMax).plus(timeToMax);
	}

}
