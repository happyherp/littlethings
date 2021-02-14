package de.carlos.funcdb

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

    override fun onNew(data: FeederData) {

        val obj = mapper.readValue(data.payload, clazz)
        collected.add(obj)
    }
}