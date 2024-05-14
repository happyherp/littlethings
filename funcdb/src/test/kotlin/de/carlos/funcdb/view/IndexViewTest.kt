package de.carlos.funcdb.view

import de.carlos.funcdb.log.InMemoryLog
import org.junit.Assert.*
import org.junit.Test
import java.nio.charset.Charset

class IndexViewTest{

    @Test
    fun testIndex(){
        val log = InMemoryLog()
        val stringView = MapView(log){it.toString(Charset.defaultCharset())}
        val lengthIndex = IndexView(stringView){it.length}

        assertEquals(emptyList<String>(), lengthIndex.findWithIndex(5))

        log.write("Hello".toByteArray())
        assertEquals(listOf("Hello"), lengthIndex.findWithIndex(5))
    }
}