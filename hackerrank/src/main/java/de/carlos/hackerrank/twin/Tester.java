package de.carlos.hackerrank.twin;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Tester {
	
    /**
     * Complete the function below.
     * DO NOT MODIFY anything outside this method. 
     */
    static boolean[] twins(String[] a, String[] b) {

        java.util.function.Function<String,List<Character>> getOddCharsSorted = s->{
            List<Character> charlist = new ArrayList<>();
            for (int i = 0;i<s.length();i+=2){
                charlist.add(s.charAt(i));
            }
            charlist.sort(Comparator.naturalOrder());
            return charlist;
        };

        java.util.function.Function<String,List<Character>> getEvenCharsSorted = s->{
            if (s.length() > 0) {
                return getOddCharsSorted.apply(s.substring(1));
            }else{
                return Collections.emptyList();
            }
        };

        boolean[] result = new boolean[a.length];
        for (int sampleIndex = 0;sampleIndex<a.length;sampleIndex++){
            String strA = a[sampleIndex];
            String strB = b[sampleIndex];

            List<Character> aEven = getEvenCharsSorted.apply(strA);
            List<Character> aOdd = getOddCharsSorted.apply(strA);

            List<Character> bEven = getEvenCharsSorted.apply(strB);
            List<Character> bOdd = getOddCharsSorted.apply(strB);

            result[sampleIndex] = aEven.equals(bEven) && aOdd.equals(bOdd);
        }
        return result;
    }

    /**
     * DO NOT MODIFY THIS METHOD!
     */
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        
        int n = Integer.parseInt(in.nextLine().trim());
        String[] a = new String[n];
        for(int i = 0; i < n; i++) {
            a[i] = in.nextLine();
        }
        
        int m = Integer.parseInt(in.nextLine().trim());
        String[] b = new String[m];
        for(int i = 0; i < m; i++) {
            b[i] = in.nextLine();
        }
        
        // call twins function
        boolean[] results = twins(a, b);
        
        for(int i = 0; i < results.length; i++) {
            System.out.println(results[i]? "Yes" : "No");
        }
    }
}