package de.carlos.simplexLife;

public class Activity {
    
    String name = "";
    
    long durationMin;
    
    long utility = 0;
    
    double cost;
    
    Long maximum = null;
    
    public Activity(int durationi, double cost, int util) {
	this.durationMin = durationi;
	this.cost = cost;
	this.utility = util;
    }

    public Activity(int durationi, double cost) {
	this.durationMin = durationi;
	this.cost = cost;
    }

    
    public double getUtilityPerMinute(){
	return ((double)this.utility) / this.durationMin;
    }
    
    public void setUtilityPerMinute(double utilityPerM){
	this.utility = (long)( utilityPerM * this.durationMin);
    }

    

    

}
