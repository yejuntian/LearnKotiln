package studykotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * GlobalScope: 是一个实现CoroutineScope的单利，生命周期和App生命周期保持一致。
 * 官网不建议使用GlobalScope，建议使用CoroutineScope()函数
 */
fun main() {
    val globalScope = GlobalScope.launch {
        delay(1000)
        println("hello")
    }
    globalScope.cancel()
    println("globalScope调用cancel()方法生命周期结束")
    Thread.sleep(2000)
}