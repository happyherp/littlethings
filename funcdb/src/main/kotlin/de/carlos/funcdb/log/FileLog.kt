package de.carlos.funcdb.log

import java.io.*
import java.nio.charset.Charset

class FileLog(private val dataDir: File) : Log {

    private var headId: StateId = InMemoryLog.Companion.FIRST_ID

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
        return headId
    }

    override fun read(from: StateId): Data {
        val sourceFile = File(dataDir, from.toString())
        return sourceFile.readBytes()
    }
}