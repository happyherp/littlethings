package de.carlos.dumbparse

import arrow.core.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PAltTest {

    val aOrB = PAlt(
        PChar('a'),
        PChar('b')
    )

    @Test
    fun matchA() {
        assertEquals('a', aOrB.parse("axxx".asSequence()).first().result.merge())
    }

    @Test
    fun matchB() {
        assertEquals('b', aOrB.parse("bxxx".asSequence()).first().result.merge())
    }

    @Test
    fun noMatch() {
        assertEquals(emptyList<ParseResult<Either<Char, Char>>>(), aOrB.parse("xxx".asSequence()))
    }

}
