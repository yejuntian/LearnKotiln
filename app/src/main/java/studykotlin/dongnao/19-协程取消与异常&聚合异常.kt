package studykotlin.dongnao

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.IOException

/**
 * 协程取消与异常
 *
 * ①取消与异常密切相关，协程内部使用CancellationException进行取消，这个异常会被忽略。
 * ②子协程取消时，不会取消它的父协程。
 * ③如果一个协程遇到了CancellationException外的其他异常，该异常会取消它的父协程；
 * 父协程会等所有子协程都结束后，父协程才会处理该异常。
 *
 * 聚合异常
 *
 * 当协程的多个子协程因为异常发生失败时，一般情况下取第一个异常进行处理。
 * 在第一个异常之后发生的其他所有异常，都会被绑定到‘第一个异常之上’。
 *
 */


fun main() {
    println("************* 子协程取消时，不会取消它的父协程 ****************")
    cancelChild()
    println("************* 父协程会等所有子协程都结束后，父协程才会处理该异常 ****************")
    cancelChild2()
    println("************* 聚合异常 ****************")
    aggregationException()
}

fun cancelChild() {
    runBlocking {
        val job = launch {
            val child = launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    println(" child is canceled")
                }
            }
            //出让执行权，让子协程执行
            yield()
            child.cancelAndJoin()
            println(" parent is working not canceled")
        }
        job.join()
    }
}

fun cancelChild2() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("catch exception is : $exception")

        }
        val scope = CoroutineScope(handler)
        val job = scope.launch(handler) {
            val child = launch {
                try {
                    delay(50)
                } finally {
                    withContext(NonCancellable) {
                        println(" child is canceled , but exception is not handled until all child terminal")
                        delay(100)
                        println("The first child finish it's not cancelable exception")
                    }
                }

                val child2 = launch {
                    println("second child throws an exception")
                    delay(10)
                    throw ArithmeticException()
                }
            }
        }
        job.join()
    }
}

fun aggregationException() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("catch exception is : $exception ${exception.suppressed.contentToString()}")

        }
        val job = GlobalScope.launch(handler) {
            launch {
                delay(10)
                throw IllegalAccessException("第1个异常")
            }

            launch {
                try {
                    delay(30)
                } finally {
                    throw IndexOutOfBoundsException("第2个异常")
                }
            }

            launch {
                try {
                    delay(300)
                } finally {
                    throw IOException("第3个异常")
                }
            }
        }
        job.join()
    }
}

