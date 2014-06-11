
public class RegExp {

    
    
    public static boolean isEmailN(String eingabe){
	if (eingabe.isEmpty() || !isValidNameChar(eingabe.charAt(0))){        //Name muss aus mindestens 
	    return false;                                                     //einem Zeichen bestehen.
	}
	int i = 1;                                                          
	while (i<eingabe.length() && isValidNameChar(eingabe.charAt(i))  ){   //Danach beliebig viele weitere	
	    i++;                                                              //Zeichen überspringen
	}
	if (i >= eingabe.length() || eingabe.charAt(i) != '@'){               //Abbrechen, wenn wir kein @ finden,  
	    return false;                                                     //oder Ende der Zeichenkette erreicht wurde
	}
	i++;
	if (i >= eingabe.length() || !isValidNameChar(eingabe.charAt(0))){    //Danach ein Domain-Zeichen,
	    return false;                                                     //sonst Abbruch
	}
	i++;
	while (i<eingabe.length() && isValidDomainChar(eingabe.charAt(i))){   //Gefolgt von beliebig vielen weiteren
	    i++;                                                              //Domainzeichen
	}
	if (i >= eingabe.length() || eingabe.charAt(i) != '.'){ 	      //Gefolgt von einem Punkt.
	    return false;
	}
	i++;	
	if (i >= eingabe.length() || !isValidDomainChar(eingabe.charAt(0))){  //Worauf wieder mindestens ein 
	    return false;                                                     //Zeichen folgen muss.
	}	
	while (i<eingabe.length() && isValidDomainChar(eingabe.charAt(i))  ){ //Aber noch beliebig viele weitere
	    i++;
	}		
	return eingabe.length() == i;                                         //Aber keine anderen Zeichen am Schluss.	
    }
    
    public static boolean isValidNameChar(char c){
	return c == '_' || c == '-' || c == '.' || (c >= 'A' && c <= 'Z')
		|| (c >= 'a' && c <= 'z');
    }   
    
    public static boolean isValidDomainChar(char c){
	return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
    
    public static boolean isEmail(String eingabe){
	return eingabe.matches("[_\\-\\.A-Za-z]+@[A-Za-z]+\\.[A-Za-z]+");
    }
    
    public static void main(String[] args){
	assertt( isEmail("carlos.freund@google.de"));
	assertt( !isEmail("!carlos.freund@google.de"));
	assertt( !isEmail("carlos.freund@google.de#"));
	assertt( !isEmail("%d@google.de"));
	assertt( !isEmail("@google.de"));
	assertt( !isEmail("carlos@.de"));
	assertt( !isEmail("carlos@google"));
	assertt( !isEmail("carlos@google."));
	System.out.println("OK");

    }
    
    public static void assertt(boolean b){
	if (!b) throw new RuntimeException();
    }

}
