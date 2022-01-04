package de.carlos.dumbparse

import arrow.core.Either
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PAltsTest{


    val pAlts = pAlts(
        "abcdef".map { PChar(it) }
    )

    @Test
    fun matchA(){
        assertEquals('a', pAlts.parse("axxx".asSequence()).single().result)
    }

    @Test
    fun matchF(){
        assertEquals('f', pAlts.parse("fxxx".asSequence()).single().result)
    }

    @Test
    fun noMatch(){
        assertEquals(emptyList<ParseResult<Char>>(), pAlts.parse("xxx".asSequence()))
    }
}