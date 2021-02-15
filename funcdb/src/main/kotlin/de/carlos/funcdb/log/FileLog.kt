package de.carlos.funcdb.log

import de.carlos.funcdb.view.ViewBase
import java.io.File

class FileLog(private val dataDir: File) : Log, ViewBase<Data>() {

    private val headFile: File = File(dataDir, "head.id")

    init {
        if (headFile.exists()) {
            headId = headFile.readText().toLong()
        } else {
            headFile.createNewFile()
        }
    }


    override fun write(data: Data): StateId {
        val newId = headId+1
        val newFile = File(dataDir, newId.toString())
        if (!newFile.createNewFile())
            throw RuntimeException("File $newFile already existed")
        newFile.writeBytes(data)
        headFile.writeText(newId.toString())
        headId = newId
        observer.fireAdd(data, currentState)
        return headId
    }

    override fun read(from: StateId): Data {
        val sourceFile = File(dataDir, from.toString())
        return sourceFile.readBytes()
    }

    override fun getAll(atState: StateId): List<Data> {
        return (InMemoryLog.FIRST_ID..currentState).map(::read)
    }
}