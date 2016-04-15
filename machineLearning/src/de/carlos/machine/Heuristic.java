package de.carlos.machine;

import java.util.List;

public interface Heuristic<INPUT, OUTPUT> {

	
	
	public OUTPUT apply(INPUT in );

	public abstract void setParameters(List<Double> parameters);

	public abstract List<Double> getParameters();
	
}
