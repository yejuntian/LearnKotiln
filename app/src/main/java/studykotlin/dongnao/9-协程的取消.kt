package studykotlin.dongnao

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 协程的取消
 * 1.取消作用域会取消它所有的子协程。
 * 2.被取消的子协程不会影响其他协程。
 * 3.协程通过抛出一个特殊的异常CancellationException()来处理取消操作
 * 4.所有kotlinx.coroutines中的挂起函数（withContext、delay等）都是可以取消的。
 */


fun main() {
    println("*********** 取消协程作用域 ****************")
    cancelCoroutineScope()
    println("*********** 被取消的子协程不会影响其他协程 ****************")
    cancelJobScope()
    println("*********** CancellationException()来处理取消操作 ****************")
    testCancellationException()
}

fun cancelCoroutineScope() {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            delay(200)
            println("job1")
        }

        scope.launch {
            delay(200)
            println("job2")
        }
        delay(100)
        scope.cancel()
        delay(500)
    }
}

fun cancelJobScope() {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        val job = scope.launch {
            delay(200)
            println("job1")
        }

        val job2 = scope.launch {
            delay(200)
            println("job2")
        }
        delay(100)
        job.cancel()
        delay(500)
    }
}

fun testCancellationException() {
    runBlocking {
        val job = launch {
            try {
                delay(1000)
                println("job1")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //delay 200毫秒，先让job协程跑一下，然后取消
        delay(200)
        //自定义异常
//        job.cancel(CancellationException("取消异常"))
        //调用join（）等待协程执行完
//        job.join()

        //等于job对象同时调用cancel()和join()方法
        job.cancelAndJoin()

    }
}














