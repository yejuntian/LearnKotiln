package studykotlin.dongnao.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * 冷流
 * Flow：是一种类似序列的冷流，flow构建器中的代码直到流收集的时候才会执行。
 *
 * Flow 可以实现一对多的情况，多个收集器可以同时收集同一个 Flow 发出的数据。
 * @see testFlowIsCode
 *
 */
fun main() {
    println("********** 测试Flow为冷流，直到收集Flow构建器中代码才会执行 ************")
    testFlowIsCode()
}

fun testFlowIsCode() = runBlocking {
    val flow = getSimpleFlow()
    println("calling collect ...")
    flow.collect {
        println(it)
    }
    println("calling collect again...")
    flow.collect {
        println(it)
    }
}

fun getSimpleFlow() = flow<Int> {
    println("Flow started")
    for (i in 1..3) {
        delay(200)
        emit(i)
    }
}

fun testOne2Many() {
    fun main() = runBlocking {
        val dataFlow: Flow<Int> = flow {
            repeat(5) {
                emit(it)
            }
        }

        val collector1 = dataFlow.collect {
            println("Collector 1 received: $it")
        }

        val collector2 = dataFlow.collect {
            println("Collector 2 received: $it")
        }
    }
}
