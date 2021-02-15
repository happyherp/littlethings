package de.carlos.funcdb.log

import de.carlos.funcdb.view.ViewBase

class InMemoryLog : ViewBase<Data>(), Log {



    companion object {
        const val FIRST_ID:StateId = -1
        val FIRST_VALUE:Data = "init".toByteArray(Charsets.UTF_8)
    }

    private val log = mutableMapOf<StateId, Data>(FIRST_ID to FIRST_VALUE)


    override fun read(id: StateId): Data {
        return log[id] ?: throw RuntimeException("Unknown State-Id: $id")
    }

    override fun write(data: Data): StateId {
        headId += 1
        log[headId] = data
        observer.fireAdd(data, currentState)
        return headId
    }

    override fun getAll(atState: StateId): List<Data> {
        if (!log.containsKey(atState)) throw RuntimeException("Unknown State: $atState")
        return log.toList()
            .takeWhile { it.first <= atState }
            .map { it.second }
    }

}