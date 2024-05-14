package de.carlos.grammar;

public class RegularVerb implements Verb {

    private String infinitiv;
    
    public RegularVerb(String infinitiv) {
	this.infinitiv = infinitiv;
	if (!this.infinitiv.endsWith("en")) {
	    throw new RuntimeException("Verbs end with 'en'.");
	}
    }


    @Override
    public SplitVerb konjugate(Person person, Time time) {
	
	String base = getRoot();

	if (time == Time.PRESENT) {
	    switch (person) {
	    case ICH:
		return new SplitVerb(base + "e");
	    case DU:
		return new SplitVerb(base + "st");
	    case ER:
		;
	    case SIE_SING:
		;
	    case ES:
		;
	    case IHR:
		return new SplitVerb(base + "t");
	    default:
		return new SplitVerb(this.infinitiv);
	    }
	}
	if (time == Time.PRETERITUM) {
	    switch (person) {
	    case ICH:
		return new SplitVerb(base + "te");
	    case DU:
		return new SplitVerb(base + "test");
	    case ER:
		;
	    case SIE_SING:
		;
	    case ES:
		return new SplitVerb(base + "te");
	    case WIR:
	    case SIE_PLURAL:
		return new SplitVerb(base + "ten");
	    case IHR:
		return new SplitVerb( base + "tet");
	    }

	}
	
	if(time == Time.FUTUR1){
	    return new SplitVerb(Verbs.WERDEN.konjugate(person, Time.PRESENT).firstPart, this.infinitiv);
	}
	
	if (time == Time.PERFEKT){
	    return new SplitVerb(Verbs.HABEN.konjugate(person, Time.PRESENT).firstPart, this.getPartizip2() );
	}
	

	throw new RuntimeException("Not yet implemented.");

    }


    protected String getRoot() {
	String base = this.infinitiv.substring(0, this.infinitiv.length() - 2);
	return base;
    }


    @Override
    public String getPartizip2() {
	return "ge"+getRoot()+"t";
    }
    

}
