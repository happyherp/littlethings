package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

class IndexView<T, K>(

    val source: View<T>,
    val indexKey: (T) -> K
) : ViewBase<T>() {


    val index:MutableMap<K, MutableList<T>> =
        source.getAll(source.currentState)
            .groupBy(indexKey)
            .mapValues { it.value.toMutableList() }
            .toMutableMap()
            .also {
                headId = source.currentState
            }

    init {
        source.observer.addListener(object :ViewChangeListener<T>{
            override fun onAdd(obj: T, state: StateId) {
                val key = indexKey(obj)
                index.putIfAbsent(key, mutableListOf())
                index[key]!!.add(obj)
                headId = state

                observer.fireAdd(obj, state)
            }
        })
    }


    fun findWithIndex(key: K): List<T>{
        return index[key]?: emptyList()
    }


    override fun getAll(atState: StateId): List<T> {
        return source.getAll(atState)
    }
}