package studykotlin.dongnao.channel

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 * 并发安全
 *（1）不安全的并发访问
 * 我们使用线程解决并发问题的时候总是遇到线程的安全问题，java平台上的kotlin协程实现
 * 避免不了存在并发调度的情况，因此线程安全同样值得留意。
 *
 * (2)协程并发安全工具
 * 除了我们在线程中常用的解决并发安全的手段之外，协程框架也提供了一些并发安全的工具。
 *
 * 如下：
 * 1.Channel：并发安全的消息通道。
 *
 * 2.Mutex：轻量级锁，它的lock和unlock从语义上和线程锁比较类似，之所以轻量
 * 是因为它在获取不到锁的时候不会造成线程阻塞，而是挂起，等待锁的释放。
 *
 *3.Semaphore:轻量级信号量，信号量可以有多个，协程在获取到信号量即可执行并发操作。
 * 当semaphore的参数为1的时，效果等价于Mutex。
 *
 * @see testSafeConcurrentMutex
 * @see testSafeConcurrentSemaphore
 *
 * (3)避免访问，外部可变状态
 * 编写函数时要求它不得访问外部状态，只能基于参数做运算，通过返回值提供运算结果
 *
 */

fun main() {
    println("****** 协程并发:不安全的案例 ******")
    testNotSafeConcurrent()

    println("****** 协程并发:安全的案例 ******")
    testSafeConcurrent()

    println("****** 协程并发安全工具:Mutex ******")
    testSafeConcurrentMutex()

    println("****** 协程并发安全工具:Semaphore ******")
    testSafeConcurrentSemaphore()

    println("****** 协程并发安全：避免外部 ******")
    safeConcurrentAccessOuterVariable()

}

fun testNotSafeConcurrent() = runBlocking {
    var count = 0
    val jobList = mutableListOf<Job>()
    List(1000) {
        val job = GlobalScope.launch {
            count++
        }
        jobList.add(job)
    }
    jobList.joinAll()
    println(count)
}

fun testSafeConcurrent() = runBlocking {
    var count = AtomicInteger()
    val jobList = mutableListOf<Job>()
    List(1000) {
        val job = GlobalScope.launch {
            count.incrementAndGet()
        }
        jobList.add(job)
    }
    jobList.joinAll()
    println(count.get())
}

fun testSafeConcurrentMutex() = runBlocking {
    var count = 0
    val mutex = Mutex()
    val jobList = arrayListOf<Job>()

    List(1000) {
        val job = GlobalScope.launch {
            mutex.withLock {
                count++
            }
        }
        jobList.add(job)
    }
    jobList.joinAll()

    println(count)
}


fun testSafeConcurrentSemaphore() = runBlocking {
    var count = 0
    // 数量为1 信号等于Mutex
    val semaphore = Semaphore(1)
    val jobList = arrayListOf<Job>()

    List(1000) {
        val job = GlobalScope.launch {
            semaphore.withPermit {
                count++
            }
        }
        jobList.add(job)
    }
    jobList.joinAll()

    println(count)
}

fun safeConcurrentAccessOuterVariable() = runBlocking {
    var count = 0
    var result = count + List(1000) {
        GlobalScope.async {
            1
        }
    }.map {
        it.await()
    }.sum()
    println(result)
}


