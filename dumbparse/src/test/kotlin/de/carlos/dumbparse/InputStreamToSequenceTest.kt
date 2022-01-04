package de.carlos.dumbparse

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.FileInputStream

class InputStreamToSequenceTest {

    @Test
    fun testReadBigFile(){
        val input = FileInputStream("C:\\Users\\carlo\\Downloads\\ideaIC-2020.3.2.exe")
        val seq = fromInputStream(input)
        println(seq.take(10).toList())
    }

    @Test
    fun readEmpty() {
        assertReadBack("")
    }

    @Test
    fun readSmall() {
        assertReadBack("Hello World")
    }

    @Test
    fun read100() {
        assertReadBack("A".repeat(100))
    }

    @Test
    fun read101() {
        assertReadBack("A".repeat(101))
    }

    @Test
    fun read200() {
        assertReadBack("A".repeat(200))
    }

    @Test
    fun read201() {
        assertReadBack("A".repeat(201))
    }

    fun assertReadBack(str: String) {
        assertEquals(
            str,
            fromInputStream(str.byteInputStream(Charsets.UTF_8))
                .toList()
                .joinToString("")
        )
    }

}