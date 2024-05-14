package anagram;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


class Result {

    /*
     * Complete the 'getMinimumDifference' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. STRING_ARRAY a
     *  2. STRING_ARRAY b
     */

    public static List<Integer> getMinimumDifference(List<String> a, List<String> b) {

        Iterator<String> iterA = a.iterator();
        Iterator<String> iterB = b.iterator();
        List<Integer> output = new ArrayList<>();

        while (iterA.hasNext() && iterB.hasNext()) {
            output.add(difference(iterA.next(), iterB.next()));
        }

        return output;
    }

    private static Integer difference(String a, String b) {
        if (a.length() != b.length()) return -1;

        //We need to not just check if B contains all of A's characters, but also the same amount of them.
        Map<Character, Integer> countsA = countChars(a);
        Map<Character, Integer> countsB = countChars(b);

        return countsA.entrySet().stream().mapToInt(entry -> {
            //How many characters of A are not in B.
            int missingInB = Math.max(0, entry.getValue() - countsB.getOrDefault(entry.getKey(), 0));
            return missingInB;
        }).sum();


    }

    private static Map<Character, Integer> countChars(String s) {
        Map<Character, Integer> counts = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            counts.putIfAbsent(c, 0);
            counts.put(c, counts.get(c) + 1);
        }
        return counts;
    }


}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int aCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> a = IntStream.range(0, aCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        int bCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> b = IntStream.range(0, bCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        List<Integer> result = Result.getMinimumDifference(a, b);

        bufferedWriter.write(
                result.stream()
                        .map(Object::toString)
                        .collect(joining("\n"))
                        + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
