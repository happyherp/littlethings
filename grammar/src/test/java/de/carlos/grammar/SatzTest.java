package de.carlos.grammar;

import org.junit.Test;
import static org.junit.Assert.*;
import static de.carlos.grammar.Time.*;

public class SatzTest {

    @Test
    public void test1() {
	HauptSatz s = new HauptSatz(Nomen.HAND, Verbs.KAUFEN, PRESENT);
	assertEquals("Die Hand kauft.", s.spell());
    }

    @Test
    public void test2() {
	HauptSatz s = new HauptSatz(Person.SIE_PLURAL, Verbs.HASSEN, PRESENT);
	assertEquals("Sie hassen.", s.spell());
    }
    
    @Test
    public void test3() {
	HauptSatz s = new HauptSatz(Person.SIE_SING, Verbs.HASSEN, PRESENT);
	assertEquals("Sie hasst.", s.spell());
    }   
    
    @Test
    public void test4() {
	HauptSatz s = new HauptSatz(Nomen.HAND, Verbs.KAUFEN, PRETERITUM);
	assertEquals("Die Hand kaufte.", s.spell());
    }
    
    @Test
    public void test5() {
	HauptSatz s = new HauptSatz(Nomen.HAND, Verbs.KAUFEN, PRETERITUM, Nomen.HAUS);
	assertEquals("Die Hand kaufte das Haus.", s.spell());
    }
    
    @Test
    public void test6() {
	HauptSatz s = new HauptSatz(Nomen.HAND, Verbs.KAUFEN, FUTUR1, Nomen.HAUS);
	assertEquals("Die Hand wird das Haus kaufen.", s.spell());
    }
    
    @Test
    public void test7() {
	HauptSatz s = new HauptSatz(Nomen.HAND, Verbs.KAUFEN, PERFEKT, Nomen.HAUS);
	assertEquals("Die Hand hat das Haus gekauft.", s.spell());
    }
    
    @Test
    public void test8() {
	HauptSatz s = new HauptSatz(new Attributsatz(Nomen.EBER, Verbs.HASSEN, FUTUR1, Nomen.HAND), Verbs.KAUFEN, PERFEKT, Nomen.HAUS);
	assertEquals("Der Eber, der die Hand hassen wird, hat das Haus gekauft.", s.spell());
    }
    

}
