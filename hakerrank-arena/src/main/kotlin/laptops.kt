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
 * Complete the 'maxCost' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER_ARRAY cost
 *  2. STRING_ARRAY labels
 *  3. INTEGER dailyCount
 */

fun maxCost(costs: Array<Int>, labels: Array<String>, dailyCount: Int): Int {


    var laptops = costs.indices.map { i-> Laptop(costs[i], labels[i] == "legal") }

    val costsOfDay = mutableListOf<Int>()
    while(laptops.filter { it.legal }.count() >= dailyCount){
        val (newLaptops, cost) = doDay(laptops, dailyCount)
        laptops = newLaptops
        costsOfDay.add(cost)
    }
    return costsOfDay.max()?:0
}


fun doDay(laptops: List<Laptop>, dailyCount: Int):Pair<List<Laptop>, Int>{
    var build = 0
    val created = laptops.takeWhile { laptop->
        val buildThis = build < dailyCount
        if (laptop.legal) build += 1
        buildThis
    }
    return Pair(laptops.drop(created.size), created.sumBy { it.cost })
}

data class Laptop(val cost:Int, val legal:Boolean)

fun main(args: Array<String>) {
    val costCount = readLine()!!.trim().toInt()

    val cost = Array<Int>(costCount, { 0 })
    for (i in 0 until costCount) {
        val costItem = readLine()!!.trim().toInt()
        cost[i] = costItem
    }

    val labelsCount = readLine()!!.trim().toInt()

    val labels = Array<String>(labelsCount, { "" })
    for (i in 0 until labelsCount) {
        val labelsItem = readLine()!!
        labels[i] = labelsItem
    }

    val dailyCount = readLine()!!.trim().toInt()

    val result = maxCost(cost, labels, dailyCount)

    println(result)
}
