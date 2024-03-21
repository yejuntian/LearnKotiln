package studykotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * launch
 * Dispatchers.IO、Dispatchers.Main、Dispatchers.Default 三个调度器都默认继承AbstractCoroutineContextElement抽象的类，
 * 而AbstractCoroutineContextElement最终都实现了CoroutineContext接口
 *
 * 第一个参数：CoroutineContext
 * 默认实现类EmptyCoroutineContext 当然我们可以指定自己的调度器Dispatchers.Default、Dispatchers.Main等等
 * 第二个参数：CoroutineStart有四中启动方式 DEFAULT(默认启动方式)、LAZY、ATOMIC(协程取消前优先执行)、UNDISPATCHED
 *
 */

@ExperimentalStdlibApi
fun main() {
    runBlocking { //可以指定自己的调度器
        launch(Dispatchers.Default) { }

        launch(CoroutineName("coroutine1")) {

            //可以通过coroutineContext获取存入类型的对象coroutine1
            val coroutineName = this.coroutineContext.get(CoroutineName)
            println("coroutineName = ${coroutineName.toString()}")
        }

        launch(Dispatchers.IO) {

            //通过coroutineContext获取存入类型对象--->Dispatchers.IO
            val dispatcher = coroutineContext.get(CoroutineDispatcher)
            println("dispatcher = ${dispatcher.toString()}")

        }


    }

    println("_________ATOMIC_________")
    atomicLauncher()

    println("_________LAZY_________")
    lazyLauncher()


}

fun atomicLauncher() {
    runBlocking {

        //协程立即执行，即使调用cancel()取消协程，也会执行该段程序
        val job = launch(start = CoroutineStart.ATOMIC) {
            println("hello")
            delay(2000)
            println("end")
        }
        println("atomicLauncher")
        job.cancel()
    }
}

fun lazyLauncher() {
    runBlocking {

        //协程立即执行，即使调用cancel()取消协程，也会执行该段程序
        val job = launch(start = CoroutineStart.LAZY) {
            println("hello")
            delay(2000)
            println("end")
        }
        println("hello coroutine")
        job.start()
    }
}