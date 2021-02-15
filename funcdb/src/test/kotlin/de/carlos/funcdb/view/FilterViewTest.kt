package de.carlos.funcdb.view

import de.carlos.funcdb.log.InMemoryLog
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.Charset

class FilterViewTest {

    @Test
    fun testFilter() {
        val log = InMemoryLog()
        val stringView = MapView(log) { bytes -> bytes.toString(Charset.defaultCharset()) }

        val filterView = FilterView(stringView) { it.first().isUpperCase() }
        assertEquals(emptyList<String>(), filterView.getAll(log.currentState))

        log.write("Hello".toByteArray())
        log.write("world".toByteArray())

        assertEquals(listOf("Hello"), filterView.getAll(log.currentState))
    }
}