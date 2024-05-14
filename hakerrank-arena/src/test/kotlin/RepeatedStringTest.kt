import org.junit.Test
import kotlin.test.assertEquals

class RepeatedStringTest {

    @Test
    fun test0(){
        assertEquals(7, repeatedString("aba", 10))
    }

    @Test
    fun test1(){
        assertEquals(1000000000000, repeatedString("a", 1000000000000))
    }

    @Test
    fun testLong(){
        assertEquals(1, repeatedString("axxa", 3))
    }

}