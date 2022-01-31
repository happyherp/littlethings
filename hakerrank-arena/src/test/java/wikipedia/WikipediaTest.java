package wikipedia;

import org.junit.Test;

import static org.junit.Assert.*;

public class WikipediaTest {

    @Test
    public void pizza() throws Exception {
        assertEquals(156, Solution.getTopicCount("pizza"));
    }

}