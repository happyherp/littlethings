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
 * Complete the 'mostActive' function below.
 *
 * The function is expected to return a STRING_ARRAY.
 * The function accepts STRING_ARRAY customers as parameter.
 */

fun mostActive(customers: Array<String>): Array<String> {

    val tradeCounts = customers
        .groupBy { it }
        .mapValues { it.value.size }

    val threshold = tradeCounts.values.sum() * 0.05;

    return tradeCounts
        .filterValues { count->count>=threshold }
        .map { it.key }
        .sorted()
        .toTypedArray()

}

fun main(args: Array<String>) {
    val customersCount = readLine()!!.trim().toInt()

    val customers = Array<String>(customersCount, { "" })
    for (i in 0 until customersCount) {
        val customersItem = readLine()!!
        customers[i] = customersItem
    }

    val result = mostActive(customers)

    println(result.joinToString("\n"))
}
