package studykotlin.dongnao

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * CoroutineStart:协程启动模式
 *
 * DEFAULT:协程创建后，立即开始调度，在调度前如果协程被取消，其将进入取消响应状态。
 *
 * ATOMIC:协程创建后，立即开始调度，协程执行到第一个挂起点之前不响应取消。
 *
 * LAZY:只有协程被需要时，包括主动调用协程的start、join、await等函数时才会调度，
 * 如果调用前就被取消，那么改协程将直接进入异常结束状态。
 *
 * UNDISPATCHED:协程创建后立即在当前函数栈中执行，直到遇到第一个真正挂起的点。
 *
 */

fun main() {
    println("*********testCoroutineJoin*******")
    testStartModel()
    println("*********testAtomicModel*******")
    testAtomicModel()
    println("********testLazyModel********")
    testLazyModel()
    println("********testUnDispatchedModel********")
    testUnDispatchedModel()
}

fun testStartModel() {
    runBlocking {
        val job = launch(start = CoroutineStart.DEFAULT) {
            delay(10000)
            println("1*****testStartModel*****")
        }
        delay(1000)
        println("2*****testStartModel*****")
        job.cancel()
    }

}


fun testAtomicModel() {
    runBlocking {
        val job = launch(start = CoroutineStart.ATOMIC) {
            println("执行第1个程序")
            println("执行第2个程序")
            println("执行第n个程序")
            println("执行到第1个挂起程序delay结束")
            delay(10000)
            println("*****testAtomicModel_1*****")
        }
        delay(1000)
        println("*****testAtomicModel_2*****")
        job.cancel()
    }

}

fun testLazyModel() {
    runBlocking {
        val job = async(start = CoroutineStart.LAZY) {
            29
        }
        println("result = ${job.await()}")
    }

}

fun testUnDispatchedModel() {
    runBlocking {
        val job = async(context = Dispatchers.IO, start = CoroutineStart.UNDISPATCHED) {
            println(Thread.currentThread().name.toString())
            println("执行到第1个挂起程序delay结束")
            delay(10000)
            23
        }
        println("***** start testUnDispatchedModel() *******")
    }

}










