package studykotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * withContext:切换线程用的,会阻塞当前协程并挂起，一直到返回结果，才会向下继续执行。
 * 第一个参数：协程上下文
 * 第二个参数  ：协程代码块
 */

fun main() {
    println("****阻塞当前所在协程****")
    withContextTest()
    println("****解决阻塞协程，再开一个协程****")
    withContextTest2()

}

@OptIn(ExperimentalStdlibApi::class)
fun withContextTest() {
    runBlocking(Dispatchers.IO) {
        println("runBlocking协程上下文：" + coroutineContext.get(CoroutineDispatcher).toString())
        val result = withContext(Dispatchers.Default) {
            delay(2000)
            println("withContext协程上下文：" + coroutineContext.get(CoroutineDispatcher).toString())

            1
        }
        println("runBlocking协程上下文：" + coroutineContext.get(CoroutineDispatcher).toString())
        println("返回结果 result = $result")
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun withContextTest2() {
    runBlocking(Dispatchers.IO) {
        println("runBlocking协程上下文：" + coroutineContext.get(CoroutineDispatcher).toString())
        launch {
            val result = withContext(Dispatchers.Default) {
                delay(2000)
                println(
                    "withContext协程上下文：" + coroutineContext.get(CoroutineDispatcher).toString()
                )

                1
            }
        }
        println("runBlocking协程上下文：" + coroutineContext.get(CoroutineDispatcher).toString())
    }
}