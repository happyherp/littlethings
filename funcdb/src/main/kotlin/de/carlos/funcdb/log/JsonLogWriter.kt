package de.carlos.funcdb.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonLogWriter(val log:Log) {

    private val mapper = jacksonObjectMapper()

    fun <T>writeObj(obj:T): StateId {
        val json = mapper.writeValueAsString(obj)
        return log.write(json.toByteArray())
    }
}