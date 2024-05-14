package de.carlos.grammar;

import de.carlos.grammar.Verb.SplitVerb;

public class IrregularVerb implements Verb {
    
    Konjugation present = null;
    
    private String partizip2;

    @Override
    public SplitVerb konjugate(Person person, Time time) {
	
	if (time == Time.PRESENT && present != null){
	    return  new SplitVerb(present.konjugate(person));
	}

	throw new RuntimeException();
    }

    
    public static class Konjugation{
	
	String[] forms;
	
	public Konjugation(String... forms){
	    this.forms = forms;
	}
	public String konjugate(Person person) {
	    
	    switch (person) {
	    case ICH:
		return this.forms[0];
	    case DU:
		return this.forms[1];
	    case ER:
		;
	    case SIE_SING:
		;
	    case ES:
		return this.forms[2];
	    case WIR:
		return this.forms[3];		    		   
	    case IHR:
		return this.forms[4];
	    case SIE_PLURAL:
		return this.forms[5];
	    }	
	    
	    throw new RuntimeException();
	} 
    }


    public void setPresent(Konjugation present) {
        this.present = present;
    }


    @Override
    public String getPartizip2() {
	return this.partizip2;
    }
    
    public void setPartizip2(String s){
	this.partizip2 = s;
    }

    
    
}
