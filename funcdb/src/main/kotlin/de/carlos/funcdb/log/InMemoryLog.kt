package de.carlos.funcdb.log

import java.lang.RuntimeException

class InMemoryLog : (Log) {

    companion object {
        const val FIRST_ID:StateId = -1
        val FIRST_VALUE:Data = "init".toByteArray(Charsets.UTF_8)
    }


    private val log = mutableMapOf<StateId, Data>(FIRST_ID to FIRST_VALUE)

    private val rootId = FIRST_ID
    private var headId = rootId

    override val currentState: StateId
        get() = headId

    override fun read(id: StateId): Data {
        return log[id] ?: throw RuntimeException("Unknown State-Id: $id")
    }

    override fun write(data: Data): StateId {
        headId += 1
        log[headId] = data
        return headId
    }

}