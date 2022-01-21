import junit.framework.TestCase
import org.junit.Test
import kotlin.test.assertEquals

class JumpingOnCloudsKtTest{

    @Test
    fun testIt1(){
        assertEquals(4,jumpingOnClouds(arrayOf(0, 0, 1, 0, 0, 1, 0)))
    }

    @Test
    fun testIt3(){
        assertEquals(1, jumpingOnClouds(arrayOf(0, 0, 0)))
    }

    @Test
    fun testIt4(){
        assertEquals(2, jumpingOnClouds(arrayOf(0, 0, 0, 0)))
    }


    @Test
    fun testItSingle(){
        assertEquals(0, jumpingOnClouds(arrayOf(0)))
    }

    @Test
    fun jumpOnPlankTest(){
        assertEquals(0, jumpsOnPlank(1))
        assertEquals(1, jumpsOnPlank(2))
        assertEquals(1, jumpsOnPlank(3))
        assertEquals(2, jumpsOnPlank(4))
        assertEquals(2, jumpsOnPlank(5))

    }
}