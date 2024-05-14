package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

interface View<T>{
    val currentState: StateId

    val observer: ViewChangeObserver<T>
}

interface ListView<T>:View<T> {

    fun getAll(atState: StateId): List<T>
}

interface SingleView<T>:View<T>{
    fun get(atState: StateId):T
}