package studykotlin.dongnao.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

/**
 * 流的上下文
 * 1.流的收集，总是在调用协程的上下文发生，流的该属性称为'上下文保存'。
 * 2.flow{...}构建器中的代码必须遵循上下文保存属性，不允许从其他上下文中发射(emit)。
 * 3.flowOn操作符，该函数用于更改流发射的上下文。
 */
fun main() {
    println("********** Flow发射流和收集流都在同一个上下文中 ************")
    testFlowContext()

    println("********** flowOn更改流发射的上下文 ************")
    testChangeFlowContext()

}

fun getSendFlow() = flow<Int> {
    (1..3).forEach {
        delay(100)
        emit(it)
        println("Flow start ${Thread.currentThread().name}  value = $it")
    }
}

fun testFlowContext() = runBlocking {
    getSendFlow().collect {
        println("Flow collect ${Thread.currentThread().name}  value = $it")
    }
}

fun getFlowCollect() = flow<Int> {
    (1..4).forEach {
        delay(300)
        emit(it)

        println("Flow start ${Thread.currentThread().name}  value = $it")
    }
}.flowOn(Dispatchers.IO)

fun testChangeFlowContext() = runBlocking {
    getFlowCollect().collect() {
        println("Flow collect ${Thread.currentThread().name}  value = $it")
    }
}
