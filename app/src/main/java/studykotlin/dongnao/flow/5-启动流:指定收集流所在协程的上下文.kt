package studykotlin.dongnao.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

/**
 * 启动流
 * 使用launchIn()函数替换collect，我们可以在指定协程中收集流的数据。
 *
 * 指定收集流所在协程的上下文
 * @see testCollectFlowContext
 *
 */
fun main() {
    println("********** 指定协程收集流的数据 ************")
    testCollectFlowContext()

}

fun getEvents() = (1..4).asFlow().onEach {
    delay(500)
}.flowOn(Dispatchers.Default)

fun testCollectFlowContext() = runBlocking {
    val flow = getEvents().onEach {
        println("Event ${Thread.currentThread().name}")
    }
    //指定在IO 上下文中收集数据
    val job = flow.launchIn(CoroutineScope(Dispatchers.IO))
    job.join()
}

