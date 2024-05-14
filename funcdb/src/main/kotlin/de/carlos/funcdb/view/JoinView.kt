package de.carlos.funcdb.view

import de.carlos.funcdb.log.StateId

class JoinView<L, R, K : Comparable<K>>(
    val sourceLeft: IndexView<L, K>,
    val sourceRight: IndexView<R, K>
) : ListView<JoinView.JoinItem<L, R, K>>, ViewBase<JoinView.JoinItem<L, R, K>>() {


    private val join: MutableMap<K, JoinItem<L, R, K>> = mutableMapOf()


    init {
        val sortedL = sourceLeft.getSorted()
        val sortedR = sourceRight.getSorted()
        val allKeys = sortedL.map { it.key }.toSet().plus(sortedR.map { it.key })
        join.putAll(allKeys
            .map { key ->
                key to JoinItem<L, R, K>(
                    sourceLeft.findWithIndex(key),
                    sourceRight.findWithIndex(key),
                    key
                )
            }
        )
        if (sourceLeft.currentState != sourceRight.currentState)
            throw RuntimeException(
                "Left Side State ${sourceLeft.currentState} is different from Right Side State " +
                        "${sourceRight.currentState}"
            )
        headId = sourceLeft.currentState

        sourceLeft.observer.addListener(object : ViewChangeListener<L> {
            override fun onAdd(obj: L, state: StateId) {
                val key = sourceLeft.indexKey(obj)
                join[key] = if (join.containsKey(key)) {
                    val oldItem = join[key]!!
                    JoinItem(oldItem.left.plus(obj), oldItem.right, key)
                } else {
                    JoinItem(listOf(obj), emptyList<R>(), key)
                }
            }
        })

        sourceRight.observer.addListener(object : ViewChangeListener<R> {
            override fun onAdd(obj: R, state: StateId) {
                val key = sourceRight.indexKey(obj)
                join[key] = if (join.containsKey(key)) {
                    val oldItem = join[key]!!
                    JoinItem(oldItem.left, oldItem.right.plus(obj), key)
                } else {
                    JoinItem(emptyList<L>(), listOf(obj) , key)
                }
            }
        })

    }

    override fun getAll(atState: StateId): List<JoinItem<L, R, K>> {
        return join.values.toList()
    }

    data class JoinItem<L, R, K>(val left: List<L>, val right: List<R>, val key: K)
}