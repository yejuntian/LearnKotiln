package studykotlin.dongnao.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking

/**
 * 操作符
 *一、过渡操作符
 * 1.过渡操作符应用于上游流，并返回下游流。
 * 2.这些操作符也是冷操作符，就像流一样。这类操作符本身不是挂起函数。
 * 3.它运行速度很快，返回新的转换流的定义。
 *
 * @see transformFlowOperator
 * @see limitLengthOperator
 *
 *二、末端操作符
 * 末端操作符是：启动流收集的挂起函数。collect是最基础的末端流操作符。
 *
 * 还有一些更方便的末端操作符。
 * 1.转为各种集合，例如toList()/toSet()
 * 2.获取第一个(first)值与确保流发射单个(single)值的操作流。
 * 3.使用reduce()与fold()将流归约到单个值。
 *
 * @see terminalOperator
 *
 * 三、组合操作符-->组合多个流
 * 就像kotlin标准库中的Sequence.zip扩展函数一样，流拥有一个zip操作符
 * 用于组合两个流中的相关值。
 *
 * @see zipOperator
 * @see zipOperator2
 *
 * 四、展平操作符
 * 很容易遇到这种情况，异步接收的每个值都会触发对另外一个值的请求，
 * 然而由于流具有异步的性质，因此需要不同的展平模式。
 *
 * flatMapContact:连接模式。
 * flatMapMerge:合并模式。
 * flatMapLast:最新展平模式。
 *
 * @see flatMapContact
 * @see flatMapMerge
 * @see flatMapLast
 */
fun main() {
    println("********** transForm操作符 ************")
    transformFlowOperator()


    println("********** take限长操作符 ************")
    limitLengthOperator()

    println("********** 末端操作符 ************")
    terminalOperator()

    println("********** 组合操作符:两个流整合为1个流 ************")
    zipOperator()

    println("********** 组合操作符:两个流整合为1个流带延时策略 ************")
    zipOperator2()

    println("********** 展平操作符->连接模式 ************")
    flatMapContact()

    println("********** 展平操作符->合并模式 ************")
    flatMapMerge()

    println("********** 展平操作符->最新展平模式 ************")
    flatMapLast()


}

suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}

fun transformFlowOperator() = runBlocking {
    (1..3).asFlow()
        .transform {
            emit("transform $it")
            emit(performRequest(it))
        }
        .collect {
            println(it)
        }
}

fun numbers() = flow<Int> {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

fun limitLengthOperator() = runBlocking {
    numbers().take(2).collect {
        println(it)
    }
}

fun terminalOperator() = runBlocking {
    val sum = (1..3).asFlow()
        .map {
            it * it
        }.reduce { a, b ->
            a + b
        }
    println("sum = $sum")
}


fun zipOperator() = runBlocking {
    val numbers = (1..3).asFlow()

    val strs = flowOf("one", "two", "three")

    numbers.zip(strs) { a, b ->
        "$a -> $b"
    }.collect {
        println(it)
    }
}

fun zipOperator2() = runBlocking {
    val numbers = (1..3)
        .asFlow()
        .onEach {
            delay(300)
        }

    val strs = flowOf("one", "two", "three")
        .onEach {
            delay(400)
        }

    val startTime = System.currentTimeMillis()
    numbers.zip(strs) { a, b ->
        "$a -> $b"
    }.collect {
        println("$it at ${System.currentTimeMillis() - startTime} ms from start")
    }
}

fun requestFlow(i: Int) = flow<String> {
    emit("$i First")
    delay(500)
    emit("$i Second")
}

fun flatMapContact() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3)
        .asFlow()
        //Flow<Flow<String>>
        //.map { requestFlow(it) }
        .flatMapConcat { requestFlow(it) }
        .onEach { delay(100) }
        .collect {
            println("$it at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

fun flatMapMerge() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3)
        .asFlow()
        //Flow<Flow<String>>
        //.map { requestFlow(it) }
        .flatMapMerge { requestFlow(it) }
        .onEach { delay(100) }
        .collect {
            println("$it at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

fun flatMapLast() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3)
        .asFlow()
        //Flow<Flow<String>>
        //.map { requestFlow(it) }
        .flatMapLatest { requestFlow(it) }
        .onEach { delay(100) }
        .collect {
            println("$it at ${System.currentTimeMillis() - startTime} ms from start")
        }
}