package de.carlos.simplexLife;

public class Alternative {
    
    Activity[] activities;
    
    int number;

    public Alternative(Activity ...activities){
	this(1, activities);
    }
    
    public Alternative(int number, Activity ...activities){
	this.activities = activities;
	this.number = number;
    }

}
