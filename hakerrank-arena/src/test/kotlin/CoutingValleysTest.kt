import org.junit.Test
import kotlin.test.assertEquals

class CoutingValleysTest {

    @Test
    fun testIt(){
        assertEquals(1, countingValleys(8, "UDDDUDUU"))
        assertEquals(0, countingValleys(8, "UUUUDDDD"))
        assertEquals(1, countingValleys(8, "DDDDUUUU"))

    }
}