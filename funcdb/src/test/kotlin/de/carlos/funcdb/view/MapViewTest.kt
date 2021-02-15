package de.carlos.funcdb.view

import de.carlos.funcdb.log.InMemoryLog
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.charset.Charset

class MapViewTest {

    @Test
    fun testMapView() {
        val log = InMemoryLog()
        val id1 = log.write("Hello".toByteArray())

        val mapView = MapView(log) { it.size }
        assertEquals(
            listOf(
                InMemoryLog.FIRST_VALUE.size,
                "Hello".toByteArray().size
            ),
            mapView.getAll(id1)
        )

        log.write("World".toByteArray())
        assertEquals(
            listOf(
                InMemoryLog.FIRST_VALUE.size,
                "Hello".toByteArray().size,
                "World".toByteArray().size
            ),
            mapView.getAll(id1)
        )
    }


    @Test
    fun testNesting(){
        val log = InMemoryLog()
        val id1 = log.write("Hello".toByteArray())

        val upperCaseMap = MapView(log) { it.toString(Charset.defaultCharset()).toUpperCase() }
        assertEquals(listOf("INIT", "HELLO"), upperCaseMap.getAll(id1))


        val firstCharOnly = MapView(upperCaseMap) { it.first() }
        assertEquals(listOf('I', 'H'), firstCharOnly.getAll(id1))

        val id2 = log.write("World".toByteArray())
        assertEquals(listOf("INIT", "HELLO", "WORLD"), upperCaseMap.getAll(id2))
        assertEquals(listOf('I', 'H', 'W'), firstCharOnly.getAll(id2))



    }


}