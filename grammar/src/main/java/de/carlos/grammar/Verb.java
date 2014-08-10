package de.carlos.grammar;

public interface Verb {

    public SplitVerb konjugate(Person person, Time time);

    public String getPartizip2();
    
    public static class SplitVerb {

	public String firstPart;
	public String secondPart;

	public SplitVerb(String first) {
	    this.firstPart = first;
	}

	public SplitVerb(String first, String second) {
	    this.firstPart = first;
	    this.secondPart = second;
	}
	
    }

}
