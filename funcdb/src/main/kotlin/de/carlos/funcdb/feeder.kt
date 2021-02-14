package de.carlos.funcdb

interface Feeder{

    fun feed(data: Data)

    fun addListener(l:FeederListener)

}

interface FeederListener {
    fun onNew(data:Data)
}

class SimpleFeeder: Feeder {

    private val listeners = mutableListOf<FeederListener>()

    val datalog = mutableListOf<Data>()

    override fun feed(data: Data) {
        datalog.add(data)
        listeners.forEach { it.onNew(data) }
    }

    override fun addListener(l: FeederListener) {
        listeners.add(l)
        datalog.forEach(l::onNew)
    }

}

class Data(
    val source:String,
    val payload:String
)

