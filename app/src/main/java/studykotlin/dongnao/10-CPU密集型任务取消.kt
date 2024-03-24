package studykotlin.dongnao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/**
 * CPU密集型任务取消
 * 1.isActive是一个在CoroutineScope中使用的拓展属性，检查Job是否处于活跃状态。
 * 2.enableActive(),如果Job处于活跃状态，这个方法立即抛出异常。
 * 3.yield()函数会检查所有协程的状态，如果已经取消，则抛出CancellationException异常，
 * 它还会尝试让出线程执行权，给其他协程提供执行机会。
 */


fun main() {
    println("*********** 使用isActive取消CPU密集型任务 ****************")
    testCpuTaskIsActive()
    println("*********** 使用ensureActive()取消CPU密集型任务 ****************")
    testCpuTaskEnsureActive()
    println("*********** 使用yield()取消CPU密集型任务 ****************")
    testCpuTaskYield()

}

fun testCpuTaskIsActive() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            // 加上isActive判断逻辑，就可以取消cpu密集型任务,直接退出协程执行程序
            while (i < 5 && isActive) {
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job i am sleeping ${i++} ...")
                    nextPrintTime += 500
                }
            }
        }
        println("main i am try canceling ...")
        job.cancelAndJoin()
    }
}

fun testCpuTaskEnsureActive() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            // 加上ensureActive()判断逻辑，就可以取消cpu密集型任务，抛异常退出协程执行程序
            ensureActive()
            while (i < 5) {
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job i am sleeping ${i++} ...")
                    nextPrintTime += 500
                }
            }
        }
        println("main i am try canceling ...")
        job.cancelAndJoin()
    }
}

fun testCpuTaskYield() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            // 加上yield()判断逻辑，就可以取消cpu密集型任务,把执行权让给其他协程。
            yield()
            while (i < 5) {
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job i am sleeping ${i++} ...")
                    nextPrintTime += 500
                }
            }
        }
        println("main i am try canceling ...")
        job.cancelAndJoin()
    }
}















