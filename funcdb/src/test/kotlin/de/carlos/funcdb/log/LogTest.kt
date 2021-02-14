package de.carlos.funcdb.log

import org.junit.Assert
import org.junit.Assert.assertArrayEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

abstract class LogTest{

    abstract fun createLog():Log

    @Test
    fun testIt(){
        val log = createLog()

        val hello = "Hello World".toByteArray()
        val helloId = log.write(hello)
        assertArrayEquals(hello, log.read(helloId))

        val bye = "Bye".toByteArray()
        val byeId = log.write(bye)
        assertArrayEquals(bye, log.read(byeId))

        val hello2Id = log.write(hello)
        assertArrayEquals(hello, log.read(hello2Id))

        assertArrayEquals(hello, log.read(helloId))
        assertArrayEquals(bye, log.read(byeId))
        assertArrayEquals(hello, log.read(hello2Id))
    }

}

class InMemoryLogTest:LogTest(){
    override fun createLog(): Log = InMemoryLog()
}

class FileLogTest:LogTest(){

    val folder = TemporaryFolder()

    @Rule
    fun folderF() = folder

    override fun createLog(): Log = FileLog(folderF().newFolder("FileLogTest"))

    @Test
    fun testPersistence(){

        val folder = folderF().newFolder("FileLogTestPerm")
        val log1 = FileLog(folder)
        val hello = "Hello".toByteArray()
        val id = log1.write(hello)

        val log2 = FileLog(folder)
        assertArrayEquals(hello, log2.read(id))
    }



}