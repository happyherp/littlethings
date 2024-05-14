package de.carlos.grammar;

public enum Person implements Substantiv{
    
    ICH("ich"), DU("du"), ER("er", Artikel.DER), SIE_SING("sie"), ES("es", Artikel.DAS), WIR("wir"), IHR("ihr"), SIE_PLURAL("sie");
    
    private String s;
    private Artikel artikel;
    
    private Person(String s){
	this(s, Artikel.DIE);
    }

    private Person(String s, Artikel artikel){
	this.s  =s;
	this.artikel = artikel;
    }

    @Override
    public String spell() {
	return this.s;
    }

    @Override
    public Person getPerson() {
	return this;
    }

    @Override
    public Artikel getArtikel() {
	return this.artikel;
    }
   
}
