package de.carlos.grammar;

import de.carlos.grammar.Verb.SplitVerb;

public class Attributsatz implements Substantiv {
    
    private Substantiv sub;
    private HauptSatz nebensatz;
    private Verb verb;
    private Time time;
    private Substantiv object;

    public Attributsatz(Substantiv sub, Verb verb, Time time, Substantiv object){
	this.sub = sub;
	this.verb = verb;
	this.time = time;
	this.object = object;
    }

    @Override
    public String spell() {
	
	
	String s = sub.spell()+ ", "+sub.getArtikel().spell();
	
	if(this.object != null){
	    s+= " " + this.object.spell();
	}
	SplitVerb splitVerb = this.verb.konjugate(sub.getPerson(), this.time);
	
	if (splitVerb.secondPart != null){
	    s+= " "+splitVerb.secondPart;
	}
	
	s+= " "+splitVerb.firstPart;
	
		
	s+=",";
	
	
	return s;
    }

    @Override
    public Person getPerson() {
	return sub.getPerson();
    }

    @Override
    public Artikel getArtikel() {
	return this.sub.getArtikel();
    }

}
