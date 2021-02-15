package de.carlos.funcdb.log

import de.carlos.funcdb.view.View

interface Log : View<Data> {

    fun write(data: Data): StateId

    fun read(from: StateId): Data
}


typealias Data = ByteArray

typealias StateId = Long