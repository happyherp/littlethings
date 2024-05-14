package de.carlos.funcdb.log

import de.carlos.funcdb.view.ViewBase
import java.time.Instant

class InMemoryLog : ViewBase<Data>(), Log {



    companion object {
        val FIRST_ID:StateId = StateId(Instant.ofEpochMilli(-1), -1)
        val FIRST_VALUE:Data = "init".toByteArray(Charsets.UTF_8)
    }

    private val dataById = mutableMapOf<StateId, Data>(FIRST_ID to FIRST_VALUE)
    private val dataLog = mutableListOf<StateId>()


    override fun read(stateId: StateId): Data {
        return dataById[stateId] ?: throw RuntimeException("Unknown State-Id: $stateId")
    }

    override fun write(data: Data): StateId {
        headId = StateId.new()
        dataById[headId] = data
        dataLog.add(headId)
        observer.fireAdd(data, currentState)
        return headId
    }

    override fun getAll(atState: StateId): List<Data> {
        if (!dataById.containsKey(atState)) throw RuntimeException("Unknown State: $atState")
        return dataById.toList()
            .takeWhile { it.first <= atState }
            .map { it.second }
    }

}