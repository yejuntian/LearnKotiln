package studykotlin.dongnao.flow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/**
 * 流的取消
 *
 * 流采取与协程同样的取消操作。
 * 像往常一样，流的收集可以是在一个挂起函数delay()挂起的时候取消。
 *
 * 流的取消检测
 * 1.流构建器对每个发射值都附加ensureActive检测进行取消的操作，
 * 这意味着从flow{...}发射出去的是可以取消的。
 *
 * 2.由于性能原因，大多数其他流操作不会自动执行取消检测，
 * 必须通过cancellable()操作符明确检查是否取消。
 *
 */
fun main() {
    println("********** 模拟流超时任务的取消 ************")
    testCancelFlow()

    println("********** 模拟检测并模拟流的取消 ************")
    //checkAndCancelFlow()

    println("********** 大多数其他流操作不会自动执行取消检测 ************")
    //testCancellableFlow()

    println("********** 使用cancelable()操作符明确检查是否取消 ************")
    testCancellableFlow2()
}

fun getSimpleFlow3() = flow<Int> {
    for (i in 1..5) {
        delay(1000)
        emit(i)
        println("Emitting $i")
    }
}

fun testCancelFlow() = runBlocking {
    // 模拟流在规定时间3000毫秒内没发送完数据的取消
    withTimeoutOrNull(3000) {
        getSimpleFlow3().collect {
            println("Collect $it")
        }
    }
    println("Done")
}


fun getSimpleFlow4() = flow<Int> {
    for (i in 1..5) {
        emit(i)
    }
}

fun checkAndCancelFlow() = runBlocking {
    getSimpleFlow4().collect {
        println("Collect $it")

        //取消流
        if (it == 3) {
            cancel()
        }
    }
}

fun testCancellableFlow() = runBlocking {
    (1..6).asFlow().collect {
        if (it == 3) {
            cancel()
        }
        println(it)
    }
}

fun testCancellableFlow2() = runBlocking {
    (1..6).asFlow().cancellable().collect {
        if (it == 3) {
            cancel()
        }
        println(it)
    }
}

