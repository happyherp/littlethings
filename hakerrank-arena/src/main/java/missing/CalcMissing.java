package missing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class CalcMissing {

    /*
     * Complete the 'calcMissing' function below.
     *
     * The function accepts STRING_ARRAY readings as parameter.
     */

    public static void calcMissing(List<String> readings) {
        List<Double> values = readings.stream().map(line -> {
            String value = line.split(" +")[2];
            if (value.contains("Missing")) return null;
            else return Double.parseDouble(value);

        }).collect(Collectors.toList());

        System.out.println(values);

    }

    public static void main(String[] args) {
        calcMissing(Arrays.asList(
                "1/4/2012 16:00:00   27.47",
                "1/4/2012 16:00:00   Missing"
                ));
    }

}