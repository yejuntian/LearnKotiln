package studykotlin.dongnao

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.FileReader

/**
 * 协程取消副作用
 * 1.在finally中释放资源。
 * 2.use()函数：该函数只能被实现了Closeable的对象使用，程序结束的时候自动调用close()方法，
 * 适合文件对象。
 */


fun main() {
    println("*********** 使用finally释放资源 ****************")
    testReleaseResources()
    println("*********** 使用use()函数释放资源 ****************")
    testUseReleaseResources()


}

fun testReleaseResources() {
    runBlocking {
        val job = launch {
            try {
                repeat(1000) {
                    println(" i am sleeping $it")
                    delay(500)
                }
            } finally {
                println("job I'm running finally ...")
            }
        }
        delay(800)
        println("main: I'm tried waiting!")
        job.cancelAndJoin()
        println("main: Now I cam quit !")
    }
}

fun testUseReleaseResources() {
    runBlocking {
        val filePath =
            "${System.getProperty("user.dir")}\\app\\src\\main\\java\\studykotlin\\dongnao\\1-协程结构化并发机制.kt"
        println(filePath)
        val bufferedReader = BufferedReader(FileReader(filePath))
        println("************传统读写方式释放资源************")
        with(bufferedReader) {
            try {
                while (true) {
                    val line = readLine() ?: break
                    println(line)
                }
            } finally {
                close()
            }
        }
        println("************使用use函数释放资源************")
        val bufferedReader2 = BufferedReader(FileReader(filePath))
        bufferedReader2.use {
            while (true){
                val line = it.readLine() ?: break
                println(line)
            }
        }
    }
}















