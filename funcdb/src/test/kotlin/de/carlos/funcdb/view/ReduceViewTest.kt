package de.carlos.funcdb.view

import de.carlos.funcdb.log.InMemoryLog
import org.junit.Assert.*
import org.junit.Test
import java.nio.charset.Charset

class ReduceViewTest {

    @Test
    fun testReduce() {

        val log = InMemoryLog()
        val stringView = MapView(log) { it.toString(Charset.defaultCharset()) }
        val charSumView = ReduceView<String, Int>(stringView, 0) { acc, new -> acc + new.length }
        assertEquals(4, charSumView.get(charSumView.currentState))

        log.write("Hello".toByteArray())
        assertEquals(9, charSumView.get(charSumView.currentState))
        log.write("World".toByteArray())
        assertEquals(14, charSumView.get(charSumView.currentState))
    }
}