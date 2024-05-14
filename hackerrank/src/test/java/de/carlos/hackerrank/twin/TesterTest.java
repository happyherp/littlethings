package de.carlos.hackerrank.twin;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TesterTest {

    @Test
    public void twins() {
        Assert.assertEquals(false,check("a", ""));
        Assert.assertEquals(true,check("", ""));

        Assert.assertEquals(true,check("a", "a"));
        Assert.assertEquals(true,check("ab", "ab"));
        Assert.assertEquals(false,check("xb", "ab"));
        Assert.assertEquals(false,check("ab", "ac"));
        Assert.assertEquals(true,check("abc", "cba"));
        Assert.assertEquals(false,check("abc", "cta"));
    }


    private boolean check(String a, String b){
        return Tester.twins(new String[]{a}, new String[]{b})[0];
    }
}