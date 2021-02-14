package de.carlos.funcdb

interface Feeder{

    fun feed(data: FeederData)

    fun addListener(l:FeederListener)

}

interface FeederListener {
    fun onNew(data:FeederData)
}

class SimpleFeeder: Feeder {

    private val listeners = mutableListOf<FeederListener>()

    val datalog = mutableListOf<FeederData>()

    override fun feed(data: FeederData) {
        datalog.add(data)
        listeners.forEach { it.onNew(data) }
    }

    override fun addListener(l: FeederListener) {
        listeners.add(l)
        datalog.forEach(l::onNew)
    }

}

class FeederData(
    val source:String,
    val payload:String
)

