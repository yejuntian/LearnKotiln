package studykotlin.dongnao

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * 协程构造器
 * launch与async构造器都用来启动新协程
 * launch：返回一个Job并且不附带任何结果值。
 * async：返回一个Deferred，Deferred也是一个Job，可以使用.await()在一个延期的值上得到最终的结果。
 * async：常用作组合并发
 *
 * join和await：等待协程，协程作用域内一直等到调用join、await的程序执行结束，才会向下执行其他协程。
 * 挂起函数不会阻塞主线程
 *
 * runBlocking:会阻塞当前主线程，一直等到协程结束。
 */

fun main() {
    println("*********testCoroutineJoin*******")
    testCoroutineBlocking()
    println("*********testCoroutineJoin*******")
    testCoroutineJoin()
    println("********testCoroutineAwait********")
    testCoroutineAwait()
    println("********组合并发********")
    testAsync()
    println("********结构化并发********")
    testCombineAsync()

}

fun testCoroutineBlocking() {
    runBlocking {
        val job = launch {
            delay(2000)
            println("one")
        }

        val job2 = launch {
            println("two")
        }


        val job3 = launch {
            println("three")
        }
    }
}

fun testCoroutineJoin() {
    runBlocking {
        val job = launch {
            delay(2000)
            println("one")
        }
        job.join()

        val job2 = launch {
            println("two")
        }


        val job3 = launch {
            println("three")
        }
    }
}

fun testCoroutineAwait() {
    runBlocking {
        val job = async {
            delay(2000)
            println("one")
        }
        job.await()

        val job2 = async {
            println("two")
        }


        val job3 = async {
            println("three")
        }
    }
}

fun testAsync() {
    runBlocking {
        val times = measureTimeMillis {
            val one = doOne()
            val two = doTwo()
            println("The result: ${one + two}")
        }
        println("complete time is $times")
    }
}

fun testCombineAsync() {
    runBlocking {
        val times = measureTimeMillis {
            val one = async { doOne() }
            val two = async { doTwo() }
            println("The result: ${one.await() + two.await()}")
        }
        println("complete time is $times")
    }
}

suspend fun doOne(): Int {
    delay(1000)
    return 14
}

suspend fun doTwo(): Int {
    delay(2000)
    return 16
}




