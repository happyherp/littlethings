package bribes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class MinimumBribes {


    public static String minimumBribes(List<Integer> q) {

        List<Integer> displacement = new ArrayList<>();
        for (int i = 0; i < q.size(); i++) {
            int supposedlyAt = q.get(i) - 1;
            int forwardmoves = supposedlyAt - i;
            if (forwardmoves > 2) return "Too chaotic";
            displacement.add(forwardmoves);
        }

        //System.out.println("people:" + q);
        //System.out.println("Displacement:" + displacement);

        int swaps = 0;
        while (!displacement.stream().allMatch(i -> i == 0)) {
            int swapWith = bestSwap(displacement);
            //System.out.println("Swapping on index:" + swapWith);
            swap(displacement, swapWith);
            displacement.set(swapWith, displacement.get(swapWith)+1);
            displacement.set(swapWith+1, displacement.get(swapWith+1)-1);
            swap(q, swapWith);
            //System.out.println("people:" + q);
            //System.out.println("Displacement:" + displacement);
            swaps++;
        }


        return ""+swaps;
    }

    private static int bestSwap(List<Integer> displacement) {
        int bestDiff = displacement.stream().mapToInt(x->x)
                .min().getAsInt();

        return displacement.indexOf(bestDiff)-1;
    }

    private static int bestSwap2(List<Integer> displacement) {
        int bestDiff = 0;
        int bestIndex = 0;
        for (int i = 0; i < displacement.size() - 1; i++) {
            int diff = displacement.get(i) - displacement.get(i + 1);
            if (diff > bestDiff) {
                bestDiff = diff;
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    public static void swap(List<Integer> a, Integer i) {
        Integer tmp = a.get(i);
        a.set(i, a.get(i + 1));
        a.set(i + 1, tmp);

    }


    /*
     * Complete the 'minimumBribes' function below.
     *
     * The function accepts INTEGER_ARRAY q as parameter.
     */

    public static String minimumBribes2(List<Integer> q) {

        int bribes = 0;
        for (int i = 0; i < q.size(); i++) {
            int supposedlyAt = q.get(i) - 1;
            int forwardmoves = supposedlyAt - i;
            if (forwardmoves > 2) return "Too chaotic";
            else if (forwardmoves > 0) bribes += forwardmoves;
        }

        return bribes + "";
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<Integer> q = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList());

                System.out.println(MinimumBribes.minimumBribes(q));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }
}
