package de.carlos.dumbparse

import arrow.core.*
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.collections.flatten


fun fromInputStream(inputStream: InputStream): ParseSource {
    val reader = inputStream.reader(Charset.forName("utf-8"))
    var buffer = CharArray(100)
    var read = reader.read(buffer)
    var returned = 0
    return generateSequence {
        if (read == -1) {
            reader.close()
            null
        } else {
            if (read > returned) {
                buffer[returned].also { returned += 1 }
            } else {
                read = reader.read(buffer)
                if (read == -1) {
                    reader.close()
                    null
                } else {
                    returned = 1
                    buffer[0]
                }
            }
        }
    }
}

typealias ParseSource = Sequence<Char>

interface PartialParse<T> {

    fun parse(input: ParseSource): List<ParseResult<T>>

}


data class ParseResult<T>(val result: T, val parseSource: ParseSource) {
    fun flat() = result to (parseSource.toList().joinToString(""))
}


class PChar(val match: Char) : PartialParse<Char> {
    override fun parse(input: ParseSource): List<ParseResult<Char>> {
        return input.firstOrNull()?.let { char ->
            if (char == match) {
                listOf(ParseResult(char, input.drop(1)))
            } else emptyList()
        } ?: emptyList()
    }
}

class PCharNot(val notMatch: List<Char>) : PartialParse<Char> {
    override fun parse(input: ParseSource): List<ParseResult<Char>> {
        return input.firstOrNull()?.let { char ->
            if (!notMatch.contains(char)) {
                listOf(ParseResult(char, input.drop(1)))
            } else emptyList()
        } ?: emptyList()
    }
}

class PNext<T, U>(val first: PartialParse<T>, val second: PartialParse<U>) : PartialParse<Pair<T, U>> {

    override fun parse(input: ParseSource): List<ParseResult<Pair<T, U>>> {
        return first.parse(input)
            .map { (firstMatch, remaining) ->
                second.parse(remaining)
                    .map { (secondMatch, remainingSecond) ->
                        ParseResult(Pair(firstMatch, secondMatch), remainingSecond)
                    }
            }.flatten()
    }
}

class PAlt<T, U>(val first: PartialParse<T>, val second: PartialParse<U>) : PartialParse<Either<T, U>> {

    override fun parse(input: ParseSource): List<ParseResult<Either<T, U>>> {

        val firstResults: List<ParseResult<Either<T, U>>> =
            first.parse(input).map { (res, rem) -> ParseResult(res.left(), rem) }
        val secondResults: List<ParseResult<Either<T, U>>> =
            second.parse(input).map { (res, rem) -> ParseResult(res.right(), rem) }

        return firstResults.plus(secondResults)
    }

}

fun <T, U> convert(parser: PartialParse<T>, convertfunction: (T) -> U): PartialParse<U> {
    return object : PartialParse<U> {
        override fun parse(input: ParseSource): List<ParseResult<U>> {
            return parser.parse(input)
                .map { (result, parseSource) -> ParseResult(convertfunction(result), parseSource) }
        }
    }
}

fun <T> pAlts(alts: List<PartialParse<T>>): PartialParse<T> {

    return when (alts.size) {
        1 -> alts.first()
        0 -> error("Must not be empty")
        else -> {
            convert(
                PAlt<T, T>(alts.first(), pAlts<T>(alts.drop(1))),
                { it.merge() })
        }
    }
}


class PNoop<T>(val token: T):PartialParse<T>{
    override fun parse(input: ParseSource): List<ParseResult<T>> = listOf(ParseResult(token, input))
}

fun <T>pOpt(option:PartialParse<T>):PartialParse<Option<T>>{
    return convert(PAlt<T, String>(option, PNoop("")), {it.getOrHandle { TODO() }} )
}


class PSeqs<T>(val parser:PartialParse<T>):PartialParse<List<T>>{

    override fun parse(input: ParseSource): List<ParseResult<List<T>>> {

        val pNext = PNext(parser, PAlt)

    }

}

