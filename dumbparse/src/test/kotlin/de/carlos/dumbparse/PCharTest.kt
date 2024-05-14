package de.carlos.dumbparse

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PCharTest {

    @Test
    fun match() {
        assertEquals(
            listOf('H' to  "ello World"),
            PChar('H').parse("Hello World".asSequence())
                .map { it.flat() }
        )
    }

    @Test
    fun noMatch(){
        assertEquals(
            listOf<Pair<Char, List<Char>>>(),
            PChar('X').parse("Hello World".asSequence()).map { it.flat() }
        )
    }


    @Test
    fun empty(){
        assertEquals(
            listOf<Pair<Char, List<Char>>>(),
            PChar('X').parse("".asSequence()).map { it.flat() }
        )
    }
}