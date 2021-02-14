package de.carlos.funcdb.log

import java.util.*

interface Log {

    fun write(data: Data): StateId

    fun read(from: StateId): Data
}


typealias Data = ByteArray

typealias StateId = Long