package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

interface View<T> {

    val currentState: StateId

    val observer: ViewChangeObserver<T>

    fun getAll(atState: StateId): List<T>

}