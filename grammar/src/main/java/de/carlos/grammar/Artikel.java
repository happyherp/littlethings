package de.carlos.grammar;

public enum Artikel implements LanguageElement{
    
    DER("der"), DIE("die"), DAS("das");
    
    private String val;

    private Artikel(String val){
	this.val = val;
    }

    @Override
    public String spell() {
	return this.val;
    }

}
