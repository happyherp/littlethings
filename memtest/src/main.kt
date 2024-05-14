import java.time.Duration
import java.time.Instant
import kotlin.random.Random

const val memoryMB = 2000
const val cellCount =  memoryMB*1000*1000 /*MB*/ / 35 /*byte to cell*/

val data:MutableList<Int> = arrayListOf()


fun main(){

    val start = Instant.now()
    for (i in 0..cellCount){
        data.add(i);
    }
    val arrayDone = Instant.now()
    println("Created array of size ${memoryMB}MB in ${Duration.between(start, arrayDone).toMillis()}ms")

    var target = Random.nextInt(cellCount*2)
    println("Searching for $target")

    for (cell in data){
        if (cell == target){
            println("Found it")
            break
        }
    }
    val done = Instant.now()
    val searchtime = Duration.between(arrayDone, done)
    println("Search took ${searchtime.toMillis()}ms")

    val speedMbPerMs = memoryMB / searchtime.toMillis()
    val speedGbPerS = speedMbPerMs
    println("Speed of search is $speedGbPerS gb/s")



}
