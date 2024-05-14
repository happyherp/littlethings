package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

class ReduceView<S, T>(
    val source: ListView<S>,
    initialValue: T,
    val reducer:(T,S)->T
) : ViewBase<T>(), SingleView<T> {


    internal var value = initialValue

    init {
        value = source.getAll(source.currentState).fold(value, reducer)
        headId = source.currentState
        source.observer.addListener(object :ViewChangeListener<S>{
            override fun onAdd(obj: S, state: StateId) {
                val newVal = reducer(value, obj)
                val oldVal = value
                value = newVal
                headId = state
                if (newVal != oldVal){
                    observer.fireChange(oldVal, newVal, state)
                }
            }

        })
    }


    override fun get(atState: StateId): T {
        if (atState != currentState) throw RuntimeException("Requested state $atState is not currentState $currentState")
        return value
    }
}