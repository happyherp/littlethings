package bribes;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class BribesPerfTest {

    @Test
    public void perfTest(){
        int n = 1000*100;
        List<Integer> input = IntStream.rangeClosed(2, n)
                .boxed().collect(Collectors.toList());
        input.add(1);
        assertEquals(""+(n-1), MinimumBribes.minimumBribes(input));
    }

}