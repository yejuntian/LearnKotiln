package studykotlin.dongnao.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

/**
 * 流的构建器
 * 1.flowOf 构建器：定义了一个发射固定值集的流。
 * 2.使用asFlow()拓展函数：可以将各种集合与序列转换为流。
 * 3.flow<Any>()函数
 *
 *@see testFlowOf
 *@see testAsFlow
 */
fun main() {
    println("********** flowOf发射固定数据集的流 ************")
    testFlowOf()

    println("********** asFlow拓展函数 ************")
    testAsFlow()
}

fun testFlowOf() = runBlocking {
    val flow = flowOf("one", "two", "three", "four")
    flow.onEach {
        //每隔500毫秒发送一个元素
        delay(500)
    }.collect {
        println(it)
    }
}

fun testAsFlow() = runBlocking {
    // 过滤奇数
    (1..5).asFlow().filter {
        it % 2 == 1
    }.map {
        "filter value is $it"
    }.collect {
        println("collect $it")
    }
}
