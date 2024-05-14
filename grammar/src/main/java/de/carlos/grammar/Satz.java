package de.carlos.grammar;

import de.carlos.grammar.Verb.SplitVerb;

public class Satz implements LanguageElement {
    
    Substantiv subject;
    
    Verb verb;
    
    Substantiv object;

    Time time;

    
    public Satz(Substantiv subject, Verb verb, Time time) {
	this(subject, verb, time, null);
    }

    
    public Satz(Substantiv subject, Verb verb, Time time, Substantiv object) {
	super();
	this.subject = subject;
	this.verb = verb;
	this.time = time;
	this.object = object;
    }

    @Override
    public String spell() {
	String s = subject.spell();
	
	SplitVerb splitVerb = this.verb.konjugate(subject.getPerson(), this.time);		
	s += " "+splitVerb.firstPart;	
	
	if (this.object != null){
	    s += " "+object.spell();
	}
	
	if (splitVerb.secondPart != null){
	    s += " "+splitVerb.secondPart;
	}
	return s;
	
    }
    
    public Substantiv getSubject() {
        return subject;
    }
    public Verb getVerb() {
        return verb;
    }
    
    
    
    public static void main(String[] args){
	
	Satz s = new HauptSatz(Nomen.EBER, Verbs.KAUFEN, Time.PRETERITUM, Nomen.HAUS);
	System.out.println(s.spell());
	
	
    }
    

}
