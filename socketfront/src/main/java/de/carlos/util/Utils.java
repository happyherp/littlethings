package de.carlos.util;

public class Utils {
    
    /**
     * .equals check, preceded by a null-check.
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Object a, Object b){
	if (a == null && b == null)  return true;
	
	if (a == null) return false;
	
	return a.equals(b);
	
    }

}
