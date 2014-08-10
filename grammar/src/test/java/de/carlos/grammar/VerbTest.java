package de.carlos.grammar;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class VerbTest {

    @Test
    public void test() {
	
	assertEquals("hassen", Verbs.HASSEN.konjugate(Person.WIR, Time.PRESENT).firstPart);
	
    }

}
