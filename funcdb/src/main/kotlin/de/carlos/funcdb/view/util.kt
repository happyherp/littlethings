package de.carlos.funcdb.view

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.carlos.funcdb.log.Data
import de.carlos.funcdb.log.InMemoryLog
import de.carlos.funcdb.log.StateId
import java.nio.charset.Charset


fun <T>jsonView(source:ListView<String>, clazz:Class<T>):MapView<String,T>{

    val mapper = jacksonObjectMapper()
    val view = MapView<String,T>(source){str->mapper.readValue(str, clazz)}
    return view
}

fun stringView(source:ListView<Data>):MapView<Data, String> = MapView(source){it.toString(Charset.defaultCharset())}

fun noInitView(source:ListView<Data>):ListView<Data> = FilterView(source){ !it.contentEquals(InMemoryLog.FIRST_VALUE)}
