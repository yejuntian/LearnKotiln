package studykotlin

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/**
 * async函数作用：
 * 1.做异步操作的,不会阻塞当前线程。
 * 2.调用await()函数的时候才会执行async()代码块内容。
 * getCompleted():获取协程是否结束，没结束会抛异常。
 *
 * select函数作用：
 * 1.统一管理async()函数异步操作
 * 2.它会阻塞当前线程，一直等待select()函数内所有协程执行完才结束。
 */
fun main() {
    runBlocking {
        val result = async {
            testAsync("1")
        }
        //println(result.getCompleted())
        println(result.await())
        println("hello Coroutine")
        println("result返回值类型：${result::class.simpleName}")

        println("*****select函数阻塞当前线程*******")
        val result2 = async {
            delay(1000)
            testAsync("result2")
        }

        val result3 = async {
            testAsync("result3")
        }
        val selectResult = select<String> {
            result2.onAwait.invoke {
                "result is 2"
            }

            result3.onAwait {
                "result is 3"
            }
        }
        println(selectResult)
    }
}

suspend fun testAsync(s: String): String {
    println("执行中：s = $s")
    delay(3000)
    return s
}

