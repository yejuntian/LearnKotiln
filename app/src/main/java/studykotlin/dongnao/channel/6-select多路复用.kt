package studykotlin.dongnao.channel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/**
 * 什么是多路复用
 *
 * 数据通信系统或计算机网络中，传输媒体的宽带或容量往往大于传输单一信号的需求，
 * 为了有效的利用通信线路，希望'一个信道同时传输多路信号'，这就是所谓的多路复用技术。
 *
 * select：多路复用，只会返回响应最快的那一个。
 * 特别注意：select代码块语句是挂起函数调用，不能使用（如 launch 或 async）协程构建器，构建新的协程作用域。
 *
 * select语句中直接使用onSend 挂起函数，而不是在select语句中使用 launch()函数构建新的作用域，在launch函数中调用onSend
 * 例如以下做法是错误的:
 *launch(Dispatchers.IO) {
 *     select<Unit?> {
 *         val sendJob1 = launch {
 *             delay(200)
 *             channels[1].onSend(200) { sentChannel ->
 *                 println("sent on $sentChannel")
 *             }
 *         }
 *         val sendJob2 = launch {
 *             delay(100)
 *             channels[0].onSend(100) { sentChannel ->
 *                 println("sent on $sentChannel")
 *             }
 *         }
 *     }
 * }
 * 解释如下：
 * 在 launch 中使用 select 语句的问题在于，launch 本身并不是一个挂起函数，而是一个协程构建器，
 * 它会在其自己的协程作用域中启动一个新的协程。这意味着在 launch 块内的代码会立即执行，
 * 而 select 语句需要的是挂起函数调用，以便它可以挂起当前协程，并等待多个挂起函数中的一个完成。
 *
 * 当你在 launch 中包裹 onSend 或者其他挂起函数时，你实际上是在一个新的协程中执行这个挂起函数，
 * 而 select 语句无法控制这个新协程的执行。因此，select 语句无法正确地选择第一个完成的挂起函数，
 * 因为它们是在不同的协程中运行的。
 *
 * 正确的做法是将 onSend、onReceive 等挂起函数直接放在 select 语句的分支中，
 * 如下所示：
 * select<Unit> {
 *     channels[0].onSend(100) {
 *         println("Channel 0 sent")
 *     }
 *     channels[1].onSend(200) {
 *         println("Channel 1 sent")
 *     }
 * }
 *
 * 这样，select 语句就可以挂起当前协程，并等待两个 onSend 挂起函数中的一个完成。
 * 一旦其中一个挂起函数完成，相应的代码块就会被执行，并且 select 语句会返回。
 * 这是 select 语句正确的使用方式，它允许你在多个可能的挂起函数中选择一个来执行。
 *
 */

fun main() {
    println("************ 多路复用案例：一个从网络请求数据，另外一个请求从本地读取 ***************")
    testAwait()

}

//声明为GlobalScope拓展函数
fun GlobalScope.getUserFromLocal(name: String) = async(Dispatchers.IO) {
    delay(1500)
    "zhangSan"
}

//声明为GlobalScope拓展函数
fun GlobalScope.getUserFromServer(name: String) = async(Dispatchers.IO) {
    delay(1000)
    "liSi"
}

data class Response<T>(val value: T, val isLocal: Boolean)

fun testAwait() = runBlocking {
    GlobalScope.launch {
        val localRequest = GlobalScope.getUserFromLocal("xxx")
        val remoteRequest = GlobalScope.getUserFromServer("xxx")

        //只返回响应最快的请求结果
        val response = select<Response<String>> {
            localRequest.onAwait {
                Response(it, true)
            }

            remoteRequest.onAwait {
                Response(it, false)
            }
        }

        response.value.let {
            println(it)
        }
    }.join()
}


