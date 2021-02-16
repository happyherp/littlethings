package de.carlos.funcdb

import de.carlos.funcdb.log.InMemoryLog
import de.carlos.funcdb.log.JsonLogWriter
import de.carlos.funcdb.view.jsonView
import de.carlos.funcdb.view.noInitView
import de.carlos.funcdb.view.stringView
import org.junit.Test
import kotlin.test.assertEquals

class JsonTest {

    @Test
    fun testJson(){

        val log = InMemoryLog()
        val jsonWriter =JsonLogWriter(log)
        val carlos = Person("Carlos", 32)
        jsonWriter.writeObj(carlos)

        val personView = jsonView(stringView(noInitView(log)), Person::class.java)
        assertEquals(listOf(carlos), personView.getAll(log.currentState))
    }

    data class Person(val name:String, val age:Int)
}