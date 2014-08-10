package de.carlos.grammar;

import static de.carlos.grammar.Artikel.*;

public class Nomen implements Substantiv{
    
    Artikel artikel;
    
    String content;

    public Nomen(Artikel artikel, String content) {
	super();
	this.artikel = artikel;
	this.content = content;
    }

    public Artikel getArtikel() {
        return artikel;
    }

    public String getContent() {
        return content;
    }
    
    @Override
    public String spell() {
	return String.format("%s %s", this.artikel.spell(), this.content);
    }


    
    public static Nomen EBER = new Nomen(DER, "Eber");
    public static Nomen HAUS = new Nomen(DAS, "Haus");
    public static Nomen HAND = new Nomen(DIE, "Hand");

    @Override
    public Person getPerson() {
	switch (this.artikel){
	case DER:
	    return Person.ER;
	case DIE:
	    return Person.SIE_SING;
	case DAS:
	    return Person.ES;
	}
	throw new IllegalStateException("Could not get Person.");
    }


    
    
}
