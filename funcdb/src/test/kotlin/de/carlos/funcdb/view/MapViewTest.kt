package de.carlos.funcdb.view

import de.carlos.funcdb.log.InMemoryLog
import org.junit.Assert.*
import org.junit.Test

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

    /*
    @Test
    fun testNesting(){
        val log = InMemoryLog()
        val id1 = log.write("Hello".toByteArray())

        val upperCaseMap = MapView(log){it.toString().toUpperCase()}
        val firstCharOnly = MapView(upperCaseMap){it}
    }

     */
}