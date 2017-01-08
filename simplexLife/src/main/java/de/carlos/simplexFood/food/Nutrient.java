package de.carlos.simplexFood.food;

public enum Nutrient {
	
	Calories,
	
	Fiber,
	
	Protein,
	Carbohydrates,
	Starch,
	Sugar,
	
	Alcohol,
	
	FatTotal,
	FatPolyUnsaturated,
	FatMonoUnsaturated,
	FatSaturated,
	
	VitaminA,
	VitaminB1,
	VitaminB2,
	VitaminB6,
	VitaminB12,
	VitaminC,
	VitaminD,
	VitaminE,
	VitaminK,
	BetaCarotene,
	PantothenicAcid,
	
	Zink,
	Folat,
	Niacin,
	Magnesium,
	Fluorid,
	Iod,
	Eisen,
	Calcium,
	Sodium,
	Potassium,
	Chloride,
	;


	public static Nutrient[] waterSolveableVitamins = {
		VitaminC, VitaminB1, VitaminB2, VitaminB6, Folat
	};
	
	public static Nutrient[] fatSolveableVitamins = {		
		VitaminA, VitaminD, VitaminE, VitaminK		
	};
	

}
