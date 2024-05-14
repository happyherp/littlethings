import java.io.*
import java.math.*
import java.security.*
import java.text.*
import java.util.*
import java.util.concurrent.*
import java.util.function.*
import java.util.regex.*
import java.util.stream.*
import kotlin.collections.*
import kotlin.comparisons.*
import kotlin.io.*
import kotlin.jvm.*
import kotlin.jvm.functions.*
import kotlin.jvm.internal.*
import kotlin.ranges.*
import kotlin.sequences.*
import kotlin.text.*

/*
 * Complete the 'jumpingOnClouds' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts INTEGER_ARRAY c as parameter.
 */

fun jumpingOnClouds(c: Array<Int>): Int {


    val planks = mutableListOf<Int>(0)
    for (cloud in c){
        if (cloud == 1){
            planks.add(0)
        }else{
            planks[planks.size-1] = planks[planks.size-1]+1
        }
    }
    val jumps = planks.map(::jumpsOnPlank).sum() + planks.size -1

    return jumps
}

fun jumpsOnPlank(plank: Int):Int = Math.floor(plank.toDouble()/2).toInt()


fun main(args: Array<String>) {
    val n = readLine()!!.trim().toInt()

    val c = readLine()!!.trimEnd().split(" ").map{ it.toInt() }.toTypedArray()

    val result = jumpingOnClouds(c)

    println(result)
}
