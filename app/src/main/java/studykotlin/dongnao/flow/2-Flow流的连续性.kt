package studykotlin.dongnao.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * 流的连续性
 * 1.流每次收集都是按顺序的，除非使用特殊符。
 * 2.从上游到下游每个操作符都会处理每个发射出的值，然后再交给末端操作符。
 *
 *@see testFlowContinuation
 *
 */
fun main() {
    println("********** 测试Flow流的连续性 ************")
    testFlowContinuation()
}

fun testFlowContinuation() = runBlocking {
    (1..5).asFlow<Int>().filter {
        it % 2 === 0
    }.map {
        "string $it"
    }.collect {
        println("collect $it")
    }
}
