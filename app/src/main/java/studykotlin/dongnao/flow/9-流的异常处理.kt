package studykotlin.dongnao.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

/**
 * 流的异常处理
 *
 * 当运算符中的发射器或代码抛出异常时，有以下异常处理方式：
 * 1.try/catch块:官方建议捕获Flow流下游异常，当然也可以捕获上游流异常，不建议这样使用。
 * 2.catch()函数:捕获Flow流上游发射数据异常。
 *
 */
fun main() {
    println("********** 流的异常处理:收集数据处理异常 ************")
    testFlowException()

    println("********** 流的异常处理:发送数据处理异常 ************")
    testFlowException2()

}

fun getFlowCollection() = flow<Int> {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

fun testFlowException() = runBlocking {
    try {
        getFlowCollection().collect {
            if (it > 2) {
                println("Collected value is $it")
                throw IllegalAccessException("收集数据发生异常")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun testFlowException2() = runBlocking {
    val flow = flow<Int> {
        for (i in 1..4) {
            if (i > 2) {
                throw IndexOutOfBoundsException("发射数据发生异常")
            }
            emit(i)
        }
    }
    flow.catch {
        println(it)
        it.printStackTrace()
    }.flowOn(Dispatchers.IO)
        .collect {
            println(it)
        }
}
