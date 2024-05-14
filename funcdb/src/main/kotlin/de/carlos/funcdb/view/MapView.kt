package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

class MapView<S, T>(
    val source: ListView<S>,
    val mapFunction: (S) -> T
) : ViewBase<T>(), ListView<T> {


    val content: MutableList<T> = source.getAll(source.currentState)
        .map(mapFunction)
        .toMutableList()
        .also { headId = source.currentState }


    init {
        source.observer.addListener(object : ViewChangeListener<S> {
            override fun onAdd(obj: S, state: StateId) {
                val mapped = mapFunction(obj)
                content.add(mapped)
                headId = state
                observer.fireAdd(mapped, state)
            }
        })
    }

    override fun getAll(atState: StateId): List<T> {
        if (atState != currentState) throw RuntimeException("Requested state $atState is not currentState $currentState")
        return content
    }


}