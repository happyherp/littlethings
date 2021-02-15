package de.carlos.funcdb.view

import de.carlos.funcdb.log.Data
import de.carlos.funcdb.log.InMemoryLog
import de.carlos.funcdb.log.Log
import de.carlos.funcdb.log.StateId
import javax.swing.plaf.nimbus.State

class MapView<T>(
    val source: Log,
    val mapFunction: (Data) -> T
) {

    val content: MutableList<T> = mutableListOf()
    private var currentState: StateId = InMemoryLog.FIRST_ID-1

    fun readFromSource() {
        while (currentState < source.currentState){
            currentState++
            content.add(mapFunction(source.read(currentState)))
        }
    }

    fun getAll(atState: StateId): List<T> {
        readFromSource()
        return content
    }


}