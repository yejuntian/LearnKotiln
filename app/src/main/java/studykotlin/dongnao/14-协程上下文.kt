package studykotlin.dongnao

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 协程上下文
 *
 * 协程上下文是什么？
 * CoroutineContext 是一组用于定义协程行为的元素。它由如下构成：
 * 1.Job:控制协程生命周期。
 * 2.CoroutineDispatcher:向合适的线程分发任务。
 * 3.CoroutineName:协程名称，调试的时候很有用。
 * 4.CoroutineExceptionHandler:处理未被捕获的异常。
 *
 *
 * 一、组合上下文元素
 *有时我们需要在协程上下文定义多个元素。我们可以使用“+”操作符来实现。
 * 比如说：我们可以隐士指定一个调度器来启动协程并且同时显式指定一个命名：
 * @testCoroutineContext()
 *
 * 二、协程上下文继承
 *
 * 对于新创建的协程，它的CoroutineContext会包含一个新的Job实例，会帮助我们控制
 * 协程的生命周期。而‘剩下的元素会从CoroutineContext的父类继承’，该父类可能是
 * 另外一个协程或者创建该协程的CoroutineContext。
 *@testCoroutineContextExtend()
 *
 * 协程的上下文 = 默认值 + 继承的CoroutineContext + 参数
 * 1.默认值：
 * 一些元素的默认值：Dispatchers.Default是默认的CoroutineDispatcher,
 * 以及‘coroutine’作为默认的CoroutineName
 *
 * 2.继承的CoroutineContext
 * 默认继承的CoroutineContext是CoroutineScope或者是父协程的CoroutineContext
 *
 * 3.参数
 * 传入协程构造器参数的优先级高于继承上下文的参数，因此会覆盖对应的参数值。
 *@testCoroutineContextExtend2()
 */


fun main() {
    println("*********** 协程上下文的组合 ****************")
    testCoroutineContext()
    println("*********** 协程上下文的继承 ****************")
    testCoroutineContextExtend()
    println("*********** 协程上下文的继承2 ****************")
    testCoroutineContextExtend2()

}

fun testCoroutineContext() = runBlocking {
    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }
}

fun testCoroutineContextExtend() = runBlocking {
    // 创建协程作用域
    val scope = CoroutineScope(Job() + Dispatchers.IO + CoroutineName("test"))
    val job = scope.launch {
        //新的协程会将scope所在的CoroutineScope作为父级
        println("${coroutineContext[Job]} ${Thread.currentThread().name}")

        val result = async {
            //通过async创建的新协程，会将当前launch协程作为父级
            println("${coroutineContext[Job]} ${Thread.currentThread().name}")
        }.await()
    }
    //等待协程执行完毕
    job.join()
}

/**
 * 协程上下文继承
 * 最终的父级CoroutineContext 会内函Dispatchers.IO而不是scope对象的Dispatchers.Main，
 * 因为它被协程构建器里的参数覆盖了，此外父级CoroutineContext里的Job是scope对象的Job，
 * 而新的Job会赋值给新协程(使用launch函数创建的协程)的CoroutineContext。
 */
fun testCoroutineContextExtend2() = runBlocking {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("catch $exception")
    }

    val scope = CoroutineScope(Job() + Dispatchers.Main + coroutineExceptionHandler)
    val job = scope.launch(Dispatchers.IO) {
        //新协程上下文：launch指定的上下文Dispatchers.IO
        println("${coroutineContext[Job]} ${Thread.currentThread().name}")
    }
    job.join()
}








