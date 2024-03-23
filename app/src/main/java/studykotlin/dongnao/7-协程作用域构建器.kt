package studykotlin.dongnao

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

/**
 * 协程作用域构建器：coroutineScope与runBlocking
 * 相同点：
 *  他们都会等待其协程体以及所有子协程结束
 * 不同点：
 *  1.runBlocking是常规函数，coroutineScope是挂起函数
 *  2.runBlocking：会阻塞当前线程来等待，
 *   coroutineScope：只是挂起，会释放底层线程用于其他操作。
 *
 * coroutineScope与supervisorScope
 * coroutineScope:一个协程失败了，其他协程也会被取消。
 * supervisorScope:一个协程失败了，不会影响其他协程。
 */

fun main() {
    println("*********一个协程失败了，不会影响其他协程*******")
    testSupervisorScope()
    println("*********个协程失败了，其他协程也会被取消*******")
    testCoroutineScope()
}

fun testCoroutineScope() {
    runBlocking {
        coroutineScope {
            val job = launch() {
                delay(200)
                println("job1 finished")
            }

            val job2 = async() {
                delay(100)
                println("job2 finished")
                throw IllegalArgumentException("主动抛异常")
            }
        }
    }
}

fun testSupervisorScope() {
    runBlocking {
        supervisorScope {
            val job = launch() {
                delay(200)
                println("job1 finished")
            }

            val job2 = async() {
                delay(100)
                println("job2 finished")
                throw IllegalArgumentException("主动抛异常")
            }
        }
    }
}













