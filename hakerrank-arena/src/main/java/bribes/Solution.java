package bribes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class MinimumBribes {

    public static class Displaced {
        /**
         * How far this person was moved from its original position
         */
        final int displacement;
        final int index;

        public Displaced(int index, int displacement) {
            this.index = index;
            this.displacement = displacement;
        }
    }

    public static class DisplacedList {

        Map<Integer, Displaced> byIndex = new HashMap<>();
        SortedMap<Integer, SortedSet<Displaced>> byDisplacement = new TreeMap<>();

        public DisplacedList(List<Integer> q) {
            for (int i = 0; i < q.size(); i++) {
                Displaced displaced = new Displaced(i, q.get(i) - 1 - i);
                insertDisplaced(displaced);
            }
        }

        private void insertDisplaced(Displaced displaced){
            byIndex.put(displaced.index, displaced);
            byDisplacement.putIfAbsent(displaced.displacement, new TreeSet<>(Comparator.comparing(d->d.index)));
            byDisplacement.get(displaced.displacement).add(displaced);
        }
        private void removeDisplaced(Displaced displaced){
            byIndex.remove(displaced.index);
            byDisplacement.get(displaced.displacement).remove(displaced);
            if (byDisplacement.get(displaced.displacement).isEmpty()){
                byDisplacement.remove(displaced.displacement);
            }
        }
        private void replace(Displaced old, Displaced newDisplaced){
            removeDisplaced(old);
            insertDisplaced(newDisplaced);
        }

        public Displaced findNext() {
            return byDisplacement.values().iterator().next().first();
        }

        public void swapLeft(Displaced right) {
            Displaced left = byIndex.get(right.index - 1);
            Displaced newLeft = new Displaced(left.index, right.displacement + 1);
            Displaced newRight = new Displaced(right.index, left.displacement - 1);

            replace(left, newLeft);
            replace(right, newRight);
        }

        public void print() {
            System.out.println(
                    "Displacement:" + byIndex.values().stream().map(x -> x.displacement).collect(Collectors.toList()));
        }

        public boolean isSorted() {
            return byDisplacement.firstKey() == 0 && byDisplacement.lastKey() == 0;
        }

        public boolean isChaotic(){
            return byDisplacement.keySet().stream().anyMatch(x->x>2);
        }
    }


    public static String minimumBribes(List<Integer> q) {

        //System.out.println("people:" + q);

        DisplacedList displacement = new DisplacedList(q);
        //displacement.print();
        if (displacement.isChaotic()) return "Too chaotic";
        int swaps = 0;

        while (!displacement.isSorted()) {
            Displaced swapWith = displacement.findNext();
            //System.out.println("Swapping index:" + swapWith.index);
            displacement.swapLeft(swapWith);
            //displacement.print();
            swaps++;
        }


        return "" + swaps;
    }

    public static String minimumBribes3(List<Integer> q) {

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
            displacement.set(swapWith, displacement.get(swapWith) + 1);
            displacement.set(swapWith + 1, displacement.get(swapWith + 1) - 1);
            swap(q, swapWith);
            //System.out.println("people:" + q);
            //System.out.println("Displacement:" + displacement);
            swaps++;
        }


        return "" + swaps;
    }

    private static int bestSwap(List<Integer> displacement) {
        int bestDiff = displacement.stream().mapToInt(x -> x)
                .min().getAsInt();

        return displacement.indexOf(bestDiff) - 1;
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
