package de.carlos.funcdb.view

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.carlos.funcdb.log.InMemoryLog
import de.carlos.funcdb.log.JsonLogWriter
import de.carlos.funcdb.view.JoinViewTest.RootObject.Author
import de.carlos.funcdb.view.JoinViewTest.RootObject.Book
import org.junit.Assert.*
import org.junit.Test

class JoinViewTest {


    @Test
    fun testJoin() {

        val log = InMemoryLog()
        val writer = JsonLogWriter(log)


        val carlos = Author("Carlos")
        val carlosBook1 = Book("Functional Databases", "Carlos")
        val carlosBook2 = Book("Functional Databases", "Carlos")
        val tim = Author("Tim")
        val timsBook = Book("Cooking stuff", "Tim")
        writer.writeObj(carlos)
        writer.writeObj(carlosBook1)
        writer.writeObj(carlosBook2)
        writer.writeObj(tim)
        writer.writeObj(timsBook)

        val jsonView = jsonView(stringView(noInitView(log)), RootObject::class.java)
        assertEquals(
            listOf(carlos, carlosBook1, carlosBook2, tim, timsBook),
            jsonView.getAll(log.currentState)
        )


        val bookView = MapView(FilterView(jsonView) { it is Book }) { it as Book }
        assertEquals(
            listOf(carlosBook1, carlosBook2, timsBook),
            bookView.getAll(bookView.currentState)
        )

        val authorView = MapView(FilterView(jsonView) { it is Author }) { it as Author }
        assertEquals(listOf(carlos, tim), authorView.getAll(authorView.currentState))

        val join = JoinView(
            IndexView(authorView) { it.name },
            IndexView(bookView) { it.authorName }
        )

        assertEquals(
            listOf(
                JoinView.JoinItem(
                    listOf(carlos),
                    listOf(carlosBook1, carlosBook2),
                    "Carlos"
                ),
                JoinView.JoinItem(
                    listOf(tim),
                    listOf(timsBook),
                    "Tim"
                )
            ),
            join.getAll(join.currentState)
        )

    }

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
    )
    sealed class RootObject {
        data class Author(val name: String) : RootObject()
        data class Book(val name: String, val authorName: String) : RootObject()
    }


}