package de.carlos.funcdb.view

import de.carlos.funcdb.log.InMemoryLog
import de.carlos.funcdb.log.StateId

abstract class ViewBase<T> : View<T> {

    internal val rootId = InMemoryLog.FIRST_ID
    internal var headId = rootId

    override val currentState: StateId
        get() = headId

    override val observer: ViewChangeObserver<T> = ViewChangeObserver()

}