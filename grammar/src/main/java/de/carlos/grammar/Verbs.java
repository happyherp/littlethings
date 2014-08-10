package de.carlos.grammar;

import de.carlos.grammar.IrregularVerb.Konjugation;


public class Verbs {
    
    public static final IrregularVerb HABEN = new IrregularVerb();
    static{
	HABEN.setPresent(new Konjugation("habe", "hast", "hat", "haben", "habt", "haben"));
    }
    
    public static Verb HASSEN = new RegularVerb("hassen"); 
    public static Verb KAUFEN = new RegularVerb("kaufen");
    
    
    public static IrregularVerb WERDEN = new IrregularVerb();
    static{
	WERDEN.setPresent(new Konjugation("werde","wirst","wird","werden","werdet","werden"));
    }
    
    public static IrregularVerb SEIN = new IrregularVerb();
    static {
	SEIN.setPresent(new Konjugation("bin","bist","ist","sind","seid","sind"));
    }
    
  

}
