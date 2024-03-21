package studykotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * runBlocking:阻塞当前线程
 */
fun main() { //kotlin 协程和线程是没有关系的，只不过协程依赖线程，调度在线程上的
    println("hello")
    val i = runBlocking(Dispatchers.IO) {
        println(Thread.currentThread().name)
        val job = this.launch {
            delay(2000)
            println(Thread.currentThread().name)
        }
        println("协程是否激活${job.isActive}")
        println("协程取消${job.cancel()}")
        println("协程是否激活${job.isActive}")
        println("second")
    }
    println("world $i")

    println("************模拟网络请求操作**************")
    netWork()
}

//模拟网络请求，无返回值lamda表达式的挂起函数
suspend fun requestNetWork(finish: suspend () -> Unit) {
    delay(1000)
    println("请求网络数据成功")
    finish()
}

//网络请求函数
fun netWork() {
    runBlocking(Dispatchers.IO) {
        requestNetWork {
            withContext(Dispatchers.Main) { //切换到主线程
                println("请求到数据，刷新UI")
            }
        }
    }
}


