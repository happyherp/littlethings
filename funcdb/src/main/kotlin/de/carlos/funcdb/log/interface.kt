package de.carlos.funcdb.log

import de.carlos.funcdb.view.ListView
import java.time.Instant
import kotlin.random.Random


//TODO: Split read/write into own interfaces
interface Log : ListView<Data> {

    fun write(data: Data): StateId

    fun read(stateId: StateId): Data
}


typealias Data = ByteArray


data class StateId(
    val time: Instant,
    val hash: Long
): Comparable<StateId> {

    companion object{
       fun new():StateId =StateId(Instant.now(), Random.nextLong())
    }

    override fun compareTo(other: StateId): Int {
        return Comparator
            .comparing(StateId::time)
            .thenComparing(StateId::hash)
            .compare(this, other)
    }

    override fun toString(): String {
        return "<${time.toEpochMilli()}:$hash>"
    }
}