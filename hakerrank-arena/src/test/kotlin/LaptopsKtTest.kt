import org.junit.Test

import org.junit.Assert.*

class LaptopsKtTest {

    @Test
    fun maxCost() {

        assertEquals(3,maxCost(arrayOf(1,2), arrayOf("illegal", "legal"), 1))

    }
}