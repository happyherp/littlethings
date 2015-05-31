package de.carlos.simplexFood;

public class Food {
    
	public int id;
    
    public String name;
    
    public Double price = null;
    
    public double kohlenhydrate;
    
    public double fett;
    
    public double protein;
    
    public double ballast;

	public double calcium;

	public double eisen;

	public double iod;

	public double fluorid;

	public double magnesium;

	public double zink;

	public double selen;

	public double vitaminA;

	public double vitaminB1;

	public double vitaminB2;

	public double vitaminB12;

	public double vitaminB6;

	public double vitaminD;

	public double vitaminC;

	public double vitaminE;

	public double niacin;

	public double folat;

    public Food(double price, double kohlenhydrate, double fett,
	    double protein, double ballast) {
	super();
	this.price = price;
	this.kohlenhydrate = kohlenhydrate;
	this.fett = fett;
	this.protein = protein;
	this.ballast = ballast;
    }
    
    
    public Food() {
    }


    public static final Food KIDNEY_BEANS = new Food(0.4/1.25, 9.08, 0.35, 5.5, 5.3);
    
}
