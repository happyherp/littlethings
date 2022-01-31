package growthInTwo;

import java.io.*;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


class Result {

    /*
     * Complete the 'countMax' function below.
     *
     * The function is expected to return a LONG_INTEGER.
     * The function accepts STRING_ARRAY upRight as parameter.
     */
    public static long countMax(List<String> upRight) {

        //The maximum value is always the number of strings, since they all cover the 1;1 field. 
        //So the question becomes: On how many cells do ALL fields contain. 


        Coord biggest = upRight.stream()
                .map(s -> { //Parse
                    String[] nums = s.split(" ");
                    return new Coord(Long.parseLong(nums[0]), Long.parseLong(nums[1]));
                })
                //Reduce the extend of all fields to the smallest
                .reduce((acc, coord) -> new Coord(
                        Math.min(acc.column, coord.column),
                        Math.min(acc.row, coord.row)))
                .get();


        //Multiply sides of the field that is included in all fields
        return biggest.column * biggest.row;
    }


    private static class Coord {
        long row;
        long column;

        Coord(long column, long row) {
            this.row = row;
            this.column = column;
        }
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int upRightCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> upRight = IntStream.range(0, upRightCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        long result = Result.countMax(upRight);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
