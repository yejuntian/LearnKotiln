package studykotlin.dongnao.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking

/**
 * 流的完成
 *
 * 当流收集完成时(普通情况或异常情况)，它可能需要执行一个动作。
 * 1.命令式finally{}块。
 * 2.onCompletion()函数处理。
 *
 */
fun main() {
    println("********** 流的完成通过finally ************")
    flowCompleteByFinally()

    println("********** 流的完成通过onCompletion ************")
    flowCompleteByOnCompletion()

    println("********** 流的完成通过onCompletion()函数，还可以查看Flow上下游流异常信息 ************")
    flowCompleteByOnCompletion2()
}

fun getSimpleFlow5() = (1..3).asFlow()

fun flowCompleteByFinally() = runBlocking {
    try {
        getSimpleFlow5().collect {
            println(it)
        }
    } finally {
        println("Done")
    }
}

fun flowCompleteByOnCompletion() = runBlocking {
    getSimpleFlow5()
        .onCompletion {
            println("Done")
        }
        .collect {
            println(it)
        }
}


fun getSimpleFlow6() = flow<Int> {
    emit(1)
    throw RuntimeException("Flow运行超时异常")
}

fun flowCompleteByOnCompletion2() = runBlocking {
    getSimpleFlow6()
        //可以获取到异常信息
        .onCompletion { exception ->
            if (exception != null) {
                println("Flow completed exception $exception")
            } else {
                println("Done")
            }
        }
        //捕获异常
        .catch {
            println("catchException $it")
        }
        .collect {
            println(it)
        }
}

