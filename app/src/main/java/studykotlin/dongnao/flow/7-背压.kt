package studykotlin.dongnao.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * 背压
 * 背压含义：水流收到和流动方向一致的压力。(比如：生产者生产的效率 > 消费者消费的效率)
 *
 * buffer():并发运行流中发射的代码。
 * conflate():合并发射项，不对每个值进行处理。
 * collectLast():取消并重新发射最后一个值。
 * 当必须更改CoroutineDispatcher时，flowOn操作符使用了相同的缓存机制，
 * 但是buffer()函数显式的请求缓冲而'不改变执行上下文'
 *
 */
fun main() {
    println("********** 背压 ************")
    backPressure()

    println("********** buffer并发发射 ************")
    backPressureBuffer()

    println("********** conflate合并发射，不对每个值进行处理 ************")
    backPressureConflate()


    println("********** collectLast()取消并重新发送最后一个值 ************")
    backPressureCollectLast()

}

fun getFlowList() = flow<Int> {
    for (i in 1..3) {
        delay(100)
        emit(i)
        println("Emitting $i ${Thread.currentThread().name}")
    }
}

fun backPressure() = runBlocking {
    val times = measureTimeMillis {
        getFlowList()
            .collect {
                delay(300)
                println("Collected $it ${Thread.currentThread().name}")
            }
    }
    println("Collected cost time is $times ms")
}

fun backPressureBuffer() = runBlocking {
    val times = measureTimeMillis {
        getFlowList()
            .buffer(50)
            .collect {
                delay(300)
                println("Collected $it ${Thread.currentThread().name}")
            }
    }
    println("Collected cost time is $times ms")
}

fun backPressureConflate() = runBlocking {
    val times = measureTimeMillis {
        getFlowList()
            .conflate()
            .collect {
                delay(300)
                println("Collected $it ${Thread.currentThread().name}")
            }
    }
    println("Collected cost time is $times ms")
}


fun backPressureCollectLast() = runBlocking {
    val times = measureTimeMillis {
        getFlowList()
            .conflate()
            .collectLatest {
                delay(300)
                println("Collected $it ${Thread.currentThread().name}")
            }
    }
    println("Collected cost time is $times ms")
}