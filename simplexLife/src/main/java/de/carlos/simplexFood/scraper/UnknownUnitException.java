package de.carlos.simplexFood.scraper;

public class UnknownUnitException extends RuntimeException {

	
	private String unit;

	public UnknownUnitException(String unit) {
		super("Dont know how to handle unit "+unit);
		this.unit = unit;
	}

}
