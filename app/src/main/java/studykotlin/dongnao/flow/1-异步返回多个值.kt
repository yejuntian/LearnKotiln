package studykotlin.dongnao.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 异步返回多个值的方案：
 * 1.集合
 * @see testMultipleValues()
 *
 * 2.序列
 * @see testSimpleSequence()
 *
 * 3.挂起函数
 * @see testMultipleValues2()
 *
 * 4.Flow
 *@see testSimpleFlow()
 *
 * Flow 与其他方式的区别
 * 1.flow{...}代码块中代码可以挂起
 * 2.使用emit()函数发射值
 * 3.使用collect()函数接收值
 */
fun main() {
    println("********** 使用集合方案:返回多个值，但不是异步 ************")
    testMultipleValues()
    println("********** 使用序列方案:返回多个值，但是同步的 ************")
    testSimpleSequence()
    println("********** 使用挂起函数:返回多个值，是异步，但是一次性返回多个值 ************")
    testMultipleValues2()
    println("********** 使用Flow函数:返回多个值，是异步 ************")
    testSimpleFlow()
}


fun testMultipleValues() {
    val simpleList = mutableListOf<Int>(1, 2, 3, 4, 5)
    simpleList.forEach {
        println(it)
    }
}

fun testSimpleSequence() {
    simpleSequence().forEach {
        println(it)
    }
}

fun simpleSequence(): Sequence<Int> = sequence {
    for (i in 1..4) {
        yield(i)
    }
}


fun testMultipleValues2() = runBlocking {
    delay(500)
    mutableListOf(1, 2, 3, 4, 5).forEach {
        println(it)
    }
}

fun testSimpleFlow() {
    val flow = flow<Int> {
        for (i in 1..5) {
            delay(500)
            emit(i)
        }
    }

    runBlocking {
        launch {
            for (i in 1..4) {
                println("我没有在阻塞线程... $i")
                delay(500)
            }
        }
        flow.collect() {
            println(it)
        }
    }

}
