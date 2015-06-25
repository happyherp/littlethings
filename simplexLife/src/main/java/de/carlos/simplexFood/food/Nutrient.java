package de.carlos.simplexFood.food;

public enum Nutrient {

	Folat,

	Niacin,

	VitaminE,

	VitaminC,

	VitaminD,

	VitaminB6,

	VitaminB12,

	VitaminB2,

	VitaminB1,

	VitaminA,
	
	VitaminK,

	Zink,

	Magnesium,

	Fluorid,

	Iod,

	Eisen,

	Calcium,

	Ballast,

	Protein,

	Fett,

	Kohlenhydrate,

	BetaCarotene,

	Sodium,

	Potassium,

	Chloride,

	PantothenicAcid,

	FatPolyUnsaturated,

	FatMonoUnsaturated,

	FatSaturated,
	
	Starch,
	
	Sugar;
	
	public static Nutrient[] waterSolveableVitamins = {
		VitaminC, VitaminB1, VitaminB2, VitaminB6, Folat
	};
	
	public static Nutrient[] fatSolveableVitamins = {		
		VitaminA, VitaminD, VitaminE, VitaminK		
	};
	

}
