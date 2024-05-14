package de.carlos.hackerrank.arrayManipulation;
import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Solution {

    /*
     * Complete the arrayManipulation function below.
     */
    static long arrayManipulationOld(int n, int[][] queries) {
        long[] array = new long[n];
        for (int[] query:queries){
            int a = query[0];
            int b = query[1];
            int k = query[2];
            for (int i = a-1;i<=b-1;i++){
                array[i] += k;
            }
            //System.out.println(Arrays.toString(array));
        }
        return Arrays.stream(array).max().getAsLong();

    }
    
    static long arrayManipulation(int n, int[][] queries) {
        Map<Integer, Long> posToChange= new HashMap<>();
        for (int[] query:queries){
            int a = query[0];
            int b = query[1];
            int k = query[2];
            posToChange.putIfAbsent(a, 0L);
            posToChange.put(a, posToChange.get(a)+k);
            
            posToChange.putIfAbsent(b+1, 0L);
            posToChange.put(b+1, posToChange.get(b+1)-k);
        }
        
        List<Long> changes = posToChange.keySet().stream()
        	.sorted()
        	.map(posToChange::get)
        	.collect(Collectors.toList());
        
        List<Long> values = new ArrayList<>();
        long currentValue = 0;
        for (Long change: changes){
        	currentValue += change;
        	values.add(currentValue);
        }
        
        return values.stream().max(Comparator.naturalOrder()).get();

    }
    

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nm = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nm[0].trim());

        int m = Integer.parseInt(nm[1].trim());

        int[][] queries = new int[m][3];

        for (int queriesRowItr = 0; queriesRowItr < m; queriesRowItr++) {
            String[] queriesRowItems = scanner.nextLine().split(" ");

            for (int queriesColumnItr = 0; queriesColumnItr < 3; queriesColumnItr++) {
                int queriesItem = Integer.parseInt(queriesRowItems[queriesColumnItr].trim());
                queries[queriesRowItr][queriesColumnItr] = queriesItem;
            }
        }

        long result = arrayManipulation(n, queries);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();
    }
}//Expected:7542539201
