package studykotlin.dongnao

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

/**
 * 超时任务
 *
 * 1.很多情况下取消协程的原因是它有可能超时。
 *
 * 2.withTimeOutOrNull 通过返回null 来进行超时操作，从而代替一个异常。
 */


fun main() {
    println("*********** 超时任务返回null ****************")
    dealWithTimeOutReturnNull()
    println("*********** 超时任务抛异常 ****************")
    dealWithTimeOut()


}

fun dealWithTimeOut() {
    runBlocking {
        withTimeout(1300) {
            repeat(1000) {
                println("job : I'm sleeping .. $it")
                delay(200)
            }
        }
        println("超时任务返回异常")
    }
}


fun dealWithTimeOutReturnNull() {
    runBlocking {
        val result = withTimeoutOrNull(1300) {
            repeat(1000) {
                println("job : I'm sleeping .. $it")
                delay(200)
            }
            "Done"
        } ?: "Jack"
        println("超时任务返回结果：result is $result")
    }

}

















