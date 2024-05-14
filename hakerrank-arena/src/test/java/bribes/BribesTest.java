package bribes;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BribesTest {

    @Test
    public void ordered() {
        assertEquals("0",
                MinimumBribes.minimumBribes(Arrays.asList(1, 2, 3, 4, 5))
        );
    }

    @Test
    public void chaotic() {
        assertEquals("Too chaotic",
                MinimumBribes.minimumBribes(Arrays.asList(4, 1, 2, 3, 5))
        );
    }

    @Test
    public void max() {
        assertEquals("5",
                MinimumBribes.minimumBribes(Arrays.asList(3,4,2,1))
        );
    }

    @Test
    public void allway() {
        assertEquals("4",
                MinimumBribes.minimumBribes(Arrays.asList(2,3,4,5,1))
        );
    }

    @Test
    public void allway2() {
        assertEquals("7",
                MinimumBribes.minimumBribes(Arrays.asList(3,4,5,2,1))
        );
    }
    //
    @Test
    public void case1() {
        assertEquals("7",
                MinimumBribes.minimumBribes(Arrays.asList(1, 2, 5, 3, 7, 8, 6, 4))
        );
    }
}