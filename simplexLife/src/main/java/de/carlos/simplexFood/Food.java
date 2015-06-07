package de.carlos.simplexFood;

import java.lang.reflect.InvocationTargetException;

public class Food implements IFood {
    
	private int id;
    
    private String name;
    
    private double weight;
    
    private Double price = null;
    
    private double kohlenhydrate;
    
    private double fett;
    
    private double protein;
    
    private double ballast;

	private double calcium;

	private double eisen;

	private double iod;

	private double fluorid;

	private double magnesium;

	private double zink;

	//private double selen;

	private double vitaminA;

	private double vitaminB1;

	private double vitaminB2;

	private double vitaminB12;

	private double vitaminB6;

	private double vitaminD;

	private double vitaminC;

	private double vitaminE;

	private double niacin;

	private double folat;

    
    public Food() {
    }


    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	@Override
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	@Override
	public double getKohlenhydrate() {
		return kohlenhydrate;
	}


	public void setKohlenhydrate(double kohlenhydrate) {
		this.kohlenhydrate = kohlenhydrate;
	}


	@Override
	public double getFett() {
		return fett;
	}


	public void setFett(double fett) {
		this.fett = fett;
	}


	@Override
	public double getProtein() {
		return protein;
	}


	public void setProtein(double protein) {
		this.protein = protein;
	}


	@Override
	public double getBallast() {
		return ballast;
	}


	public void setBallast(double ballast) {
		this.ballast = ballast;
	}


	@Override
	public double getCalcium() {
		return calcium;
	}


	public void setCalcium(double calcium) {
		this.calcium = calcium;
	}


	@Override
	public double getEisen() {
		return eisen;
	}


	public void setEisen(double eisen) {
		this.eisen = eisen;
	}


	@Override
	public double getIod() {
		return iod;
	}


	public void setIod(double iod) {
		this.iod = iod;
	}


	@Override
	public double getFluorid() {
		return fluorid;
	}


	public void setFluorid(double fluorid) {
		this.fluorid = fluorid;
	}


	@Override
	public double getMagnesium() {
		return magnesium;
	}


	public void setMagnesium(double magnesium) {
		this.magnesium = magnesium;
	}


	@Override
	public double getZink() {
		return zink;
	}


	public void setZink(double zink) {
		this.zink = zink;
	}


	@Override
	public double getVitaminA() {
		return vitaminA;
	}


	public void setVitaminA(double vitaminA) {
		this.vitaminA = vitaminA;
	}


	@Override
	public double getVitaminB1() {
		return vitaminB1;
	}


	public void setVitaminB1(double vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}


	@Override
	public double getVitaminB2() {
		return vitaminB2;
	}


	public void setVitaminB2(double vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}


	@Override
	public double getVitaminB12() {
		return vitaminB12;
	}


	public void setVitaminB12(double vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}


	@Override
	public double getVitaminB6() {
		return vitaminB6;
	}


	public void setVitaminB6(double vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}


	@Override
	public double getVitaminD() {
		return vitaminD;
	}


	public void setVitaminD(double vitaminD) {
		this.vitaminD = vitaminD;
	}


	@Override
	public double getVitaminC() {
		return vitaminC;
	}


	public void setVitaminC(double vitaminC) {
		this.vitaminC = vitaminC;
	}


	@Override
	public double getVitaminE() {
		return vitaminE;
	}


	public void setVitaminE(double vitaminE) {
		this.vitaminE = vitaminE;
	}


	@Override
	public double getNiacin() {
		return niacin;
	}


	public void setNiacin(double niacin) {
		this.niacin = niacin;
	}


	@Override
	public double getFolat() {
		return folat;
	}


	public void setFolat(double folat) {
		this.folat = folat;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


    
}
