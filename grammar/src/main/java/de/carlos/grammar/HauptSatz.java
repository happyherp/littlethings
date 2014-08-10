package de.carlos.grammar;


public class HauptSatz extends Satz{
    
    public HauptSatz(Substantiv subject, Verb verb, Time time) {
	super(subject, verb, time);
    }

    public HauptSatz(Substantiv subject, Verb verb, Time time, Substantiv object) {
	super(subject, verb, time, object);
    }

    @Override
    public String spell() {
	
	String s = super.spell();
	//Capitalize and add Dot.
	return s.substring(0, 1).toUpperCase()+ s.substring(1)+ ".";
    }



}
