package de.carlos.funcdb

import de.carlos.funcdb.Data
import de.carlos.funcdb.SimpleFeeder
import org.junit.Assert
import org.junit.Test

class SimpleFeederTest{

    @Test
    fun testFeed(){

        val feeder = SimpleFeeder()
        feeder.feed(Data("inmem","Hello World"))

        val listener = object : FeederListener {
            var wordcount = 0
            override fun onNew(data: Data) {
                wordcount += data.payload.split(" ").size
            }
        }
        feeder.addListener(listener)
        Assert.assertEquals(2, listener.wordcount)

        feeder.feed(Data("inmem","Bye Bye, World"))
        Assert.assertEquals(5, listener.wordcount)
    }

    @Test
    fun testStudents(){

        val feeder = SimpleFeeder()
        val students = JacksonListener(Student::class.java)
        feeder.addListener(students)

        feeder.feed(Data("/students", """
            {"id":1, "name":"Carlos"}
        """.trimIndent()))

        Assert.assertEquals(1, students.collected.size)
        Assert.assertEquals("Carlos", students.collected.first().name)

    }


    data class Student(val id:Int, val name:String)
}