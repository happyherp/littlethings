package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

class ViewChangeObserver<T> {

    val listeners = mutableListOf<ViewChangeListener<T>>()

    fun addListener(listener: ViewChangeListener<T>) = listeners.add(listener)

    fun fireAdd(obj: T, state: StateId) {
        listeners.forEach { it.onAdd(obj, state) }
    }

    fun fireChange(oldVal: T, newVal: T, state: StateId) {
        listeners.forEach { it.onChange(oldVal, newVal, state) }
    }


}

interface ViewChangeListener<T> {
    fun onAdd(obj: T, state: StateId)
    fun onChange(oldVal: T, newVal: T, state: StateId) {
        throw NotImplementedError()
    }
}