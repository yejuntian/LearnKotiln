package studykotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * GlobalScope: 是一个实现CoroutineScope的单利，生命周期和App生命周期保持一致。
 * 官网不建议使用GlobalScope，建议使用CoroutineScope()函数
 */
fun main() {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch {
        delay(1000)
        println("hello")
    }

    coroutineScope.launch {
        delay(1000)
        println("world")
    }

    //取消协程
    coroutineScope.cancel()
    Thread.sleep(3000)
}