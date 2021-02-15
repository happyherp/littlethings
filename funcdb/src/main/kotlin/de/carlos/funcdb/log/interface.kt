package de.carlos.funcdb.log

interface Log {

    val currentState: StateId

    fun write(data: Data): StateId

    fun read(from: StateId): Data
}


typealias Data = ByteArray

typealias StateId = Long