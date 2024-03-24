package studykotlin.dongnao

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 协程异常捕获
 * 使用CoroutineExceptionHandler对协程的异常进行捕获
 *
 * 下面条件满足时，异常就会捕获
 * 1.时机：
 * 异常被自动抛出异常的协程所抛出的（使用launch而不是async）
 * @see testCoroutineExceptionHandler()
 * 2.位置：
 * ①在CoroutineScope的CoroutineContext上下文中；
 * ②或在一个根协程（CoroutineScope或supervisorScope）或协程作用域的直接子协程中
 *
 */


fun main() {
    println("*********** 异常捕获——>被主动抛出异常的协程 ****************")
    testCoroutineExceptionHandler()

    println("*********** 异常捕获2——>被主动抛出异常的协程 ****************")
    testCoroutineExceptionHandler2()

    println("*********** 异常未被捕获——>被主动抛出异常的协程所抛出的 ****************")
    testCoroutineExceptionHandler3()


}

fun testCoroutineExceptionHandler() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("catch exception $exception")

        }
        val scope = CoroutineScope(handler)
        val job = scope.launch {
            throw AssertionError()
        }

        val deferred = scope.async {
            throw ArithmeticException()
        }

        job.join()
        deferred.await()
    }
}


fun testCoroutineExceptionHandler2() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("catch exception $exception")

        }
        val scope = CoroutineScope(Job())
        val job = scope.launch(handler) {
            throw AssertionError()
        }

        val deferred = scope.async {
            throw ArithmeticException()
        }

        job.join()
        deferred.await()
    }
}


fun testCoroutineExceptionHandler3() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("catch exception $exception")

        }
        val scope = CoroutineScope(Job())
        val job = scope.launch {
            launch(handler) {
                throw AssertionError()
            }

        }
        job.join()
    }
}




