package de.carlos.funcdb.log

import de.carlos.funcdb.view.ListView

interface Log : ListView<Data> {

    fun write(data: Data): StateId

    fun read(stateId: StateId): Data
}


typealias Data = ByteArray

typealias StateId = Long