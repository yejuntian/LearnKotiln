package studykotlin

import kotlinx.coroutines.*

fun main() { //kotlin 协程和线程是没有关系的，只不过协程依赖线程，调度在线程上的
    CoroutineScope(Dispatchers.IO).launch {
        test()
    }
    println("主线程")
    Thread.sleep(2000)
}


/**
 * 挂起函数：可以说是协程代码块
 * 挂起函数必须在协程作用域进行调度。
 */


suspend fun test() {
    println("协程开始")
    delay(1000)
    println("协程结束")
    withContext(Dispatchers.Main) {
        println(Thread.currentThread().name)
    }
}