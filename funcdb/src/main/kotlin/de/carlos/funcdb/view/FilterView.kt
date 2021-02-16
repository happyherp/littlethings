package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

class FilterView<T>(
    val source: ListView<T>,
    val filter: (T) -> Boolean
) : ViewBase<T>(), ListView<T> {

    private val filtered: MutableList<T> = source.getAll(source.currentState)
        .filter(filter)
        .toMutableList()
        .also { headId = source.currentState }

    init {
        source.observer.addListener(object : ViewChangeListener<T> {
            override fun onAdd(obj: T, state: StateId) {
                if (filter(obj)) {
                    filtered.add(obj)
                    observer.fireAdd(obj, state)
                }
                headId = state
            }
        })
    }

    override fun getAll(atState: StateId): List<T> {
        if (atState != currentState) throw RuntimeException("Requested state $atState is not currentState $currentState")
        return filtered
    }
}