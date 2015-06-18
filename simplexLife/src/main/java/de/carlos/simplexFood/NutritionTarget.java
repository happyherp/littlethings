package de.carlos.simplexFood;

public class NutritionTarget {
	


    private Limit kohlenhydrate = null;
    
    private Limit fett = null;
    
    private Limit fatSaturated = null;
    
    private Limit fatMonoUnsaturated = null;
    
    private Limit fatPolyUnsaturated = null;
    
    private Limit protein = null;
    
    private Limit ballast = null;

	private Limit calcium = null;

	private Limit eisen = null;

	private Limit iod = null;

	private Limit fluorid = null;

	private Limit magnesium = null;

	private Limit zink = null;

	private Limit vitaminA = null;
	
	private Limit betaCarotene = null;

	private Limit vitaminB1 = null;

	private Limit vitaminB2 = null;

	private Limit vitaminB12 = null;

	private Limit vitaminB6 = null;

	private Limit vitaminD = null;

	private Limit vitaminC = null;

	private Limit vitaminE = null;

	private Limit niacin = null;

	private Limit folat = null;
	
	private Limit pantothenicAcid = null;
	
	private Limit sodium = null;
	
	private Limit potassium = null;
	
	private Limit chloride = null;
	
	public Limit getKohlenhydrate() {
		return kohlenhydrate;
	}
 
	public void setKohlenhydrate(Limit kohlenhydrate) {
		this.kohlenhydrate = kohlenhydrate;
	}




	public Limit getFett() {
		return fett;
	}




	public void setFett(Limit fett) {
		this.fett = fett;
	}




	public Limit getFatSaturated() {
		return fatSaturated;
	}




	public void setFatSaturated(Limit fatSaturated) {
		this.fatSaturated = fatSaturated;
	}




	public Limit getFatMonoUnsaturated() {
		return fatMonoUnsaturated;
	}




	public void setFatMonoUnsaturated(Limit fatMonoUnsaturated) {
		this.fatMonoUnsaturated = fatMonoUnsaturated;
	}




	public Limit getFatPolyUnsaturated() {
		return fatPolyUnsaturated;
	}




	public void setFatPolyUnsaturated(Limit fatPolyUnsaturated) {
		this.fatPolyUnsaturated = fatPolyUnsaturated;
	}




	public Limit getProtein() {
		return protein;
	}




	public void setProtein(Limit protein) {
		this.protein = protein;
	}




	public Limit getBallast() {
		return ballast;
	}




	public void setBallast(Limit ballast) {
		this.ballast = ballast;
	}




	public Limit getCalcium() {
		return calcium;
	}




	public void setCalcium(Limit calcium) {
		this.calcium = calcium;
	}




	public Limit getEisen() {
		return eisen;
	}




	public void setEisen(Limit eisen) {
		this.eisen = eisen;
	}




	public Limit getIod() {
		return iod;
	}




	public void setIod(Limit iod) {
		this.iod = iod;
	}




	public Limit getFluorid() {
		return fluorid;
	}




	public void setFluorid(Limit fluorid) {
		this.fluorid = fluorid;
	}




	public Limit getMagnesium() {
		return magnesium;
	}




	public void setMagnesium(Limit magnesium) {
		this.magnesium = magnesium;
	}




	public Limit getZink() {
		return zink;
	}




	public void setZink(Limit zink) {
		this.zink = zink;
	}




	public Limit getVitaminA() {
		return vitaminA;
	}




	public void setVitaminA(Limit vitaminA) {
		this.vitaminA = vitaminA;
	}




	public Limit getBetaCarotene() {
		return betaCarotene;
	}




	public void setBetaCarotene(Limit betaCarotene) {
		this.betaCarotene = betaCarotene;
	}




	public Limit getVitaminB1() {
		return vitaminB1;
	}




	public void setVitaminB1(Limit vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}




	public Limit getVitaminB2() {
		return vitaminB2;
	}




	public void setVitaminB2(Limit vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}




	public Limit getVitaminB12() {
		return vitaminB12;
	}




	public void setVitaminB12(Limit vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}




	public Limit getVitaminB6() {
		return vitaminB6;
	}




	public void setVitaminB6(Limit vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}




	public Limit getVitaminD() {
		return vitaminD;
	}




	public void setVitaminD(Limit vitaminD) {
		this.vitaminD = vitaminD;
	}




	public Limit getVitaminC() {
		return vitaminC;
	}




	public void setVitaminC(Limit vitaminC) {
		this.vitaminC = vitaminC;
	}




	public Limit getVitaminE() {
		return vitaminE;
	}




	public void setVitaminE(Limit vitaminE) {
		this.vitaminE = vitaminE;
	}




	public Limit getNiacin() {
		return niacin;
	}




	public void setNiacin(Limit niacin) {
		this.niacin = niacin;
	}




	public Limit getFolat() {
		return folat;
	}




	public void setFolat(Limit folat) {
		this.folat = folat;
	}




	public Limit getPantothenicAcid() {
		return pantothenicAcid;
	}




	public void setPantothenicAcid(Limit pantothenicAcid) {
		this.pantothenicAcid = pantothenicAcid;
	}




	public Limit getSodium() {
		return sodium;
	}




	public void setSodium(Limit sodium) {
		this.sodium = sodium;
	}




	public Limit getPotassium() {
		return potassium;
	}




	public void setPotassium(Limit potassium) {
		this.potassium = potassium;
	}




	public Limit getChloride() {
		return chloride;
	}




	public void setChloride(Limit chloride) {
		this.chloride = chloride;
	}




	public class Limit{
		
		Double min;
		Double max;
		
		
		public Limit(Double min){
			this.min = min;
		}
		
		public Limit(Double min, Double max){
			this.min = min;
			this.max = max;
		}
		
		
		
	}

}
