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


