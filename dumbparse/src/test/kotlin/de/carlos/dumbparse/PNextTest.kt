package de.carlos.dumbparse

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PNextTest{


    val ab = PNext(PChar('a'), PChar('b'))


    @Test
    fun match() {
        assertEquals(
            listOf('a' to 'b' to  "er"),
            ab.parse("aber".asSequence())
                .map { it.flat() }
        )
    }

    @Test
    fun firstMatchOnly(){
        assertEquals(
            listOf<Pair<Char, List<Char>>>(),
            ab.parse("aver".asSequence()).map { it.flat() }
        )
    }

    @Test
    fun empty(){
        assertEquals(
            listOf<Pair<Char, List<Char>>>(),
            ab.parse("".asSequence()).map { it.flat() }
        )
    }

    @Test
    fun oneChar(){
        assertEquals(
            listOf<Pair<Char, List<Char>>>(),
            ab.parse("a".asSequence()).map { it.flat() }
        )
    }

}