package de.carlos.funcdb

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule


fun makeMapper():ObjectMapper{
    val mapper =  ObjectMapper()
    mapper.registerModule(KotlinModule())
    return mapper
}
val mapper = makeMapper()



class JacksonListener<T>(val clazz:Class<T>):FeederListener {
    val collected = mutableListOf<T>()

    override fun onNew(data: Data) {

        val obj = mapper.readValue(data.payload, clazz)
        collected.add(obj)
    }
}