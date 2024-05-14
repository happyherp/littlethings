package de.carlos.funcdb.log

import de.carlos.funcdb.view.ViewBase
import java.io.File
import java.lang.IllegalArgumentException
import java.time.Instant
import kotlin.random.Random

class FileLog(private val dataDir: File) : Log, ViewBase<Data>() {

    private val headFile: File = File(dataDir, "head.id")

    init {

        if (headFile.exists()) {
            headId = readAllIds().last()
        } else {
            headFile.createNewFile()
            headFile.writeText(headId.toString()+"\n")
        }
    }

    private fun readAllIds():List<StateId> = headFile.readText().split("\n")
        .filter { it.isNotEmpty() }
        .map(::readId)

    fun createFilename(stateId: StateId):String{
        return stateId.time.toEpochMilli().toString()+"_"+stateId.hash+".dat"
    }

    override fun write(data: Data): StateId {
        val newId = StateId.new()
        val newFile = File(dataDir, createFilename(newId))
        if (!newFile.createNewFile())
            throw RuntimeException("File $newFile already existed")
        newFile.writeBytes(data)
        headFile.appendText(newId.toString()+"\n")
        headId = newId
        observer.fireAdd(data, currentState)
        return headId
    }

    override fun read(stateId: StateId): Data {
        val sourceFile = File(dataDir, createFilename(stateId))
        return sourceFile.readBytes()
    }

    override fun getAll(atState: StateId): List<Data> {
        return readAllIds().map(::read)
    }

    fun readId(serialized:String):StateId{
        val m = "^<(-?\\d+):(-?\\d+)>$".toPattern().matcher(serialized)
        if (m.matches()){
            return StateId(Instant.ofEpochMilli(m.group(1).toLong()),
                m.group(2).toLong())
        }else{
            throw IllegalArgumentException("Could not parse $serialized")
        }

    }
}