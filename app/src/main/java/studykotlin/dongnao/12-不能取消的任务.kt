package studykotlin.dongnao

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * 不能取消的任务
 *
 * 1.处于取消状态的协程不能被挂起（运行不能取消的代码），当协程取消后需要调用挂起函数（比如 withContext），
 * 将清理任务的代码置于NonCancellable CoroutineContext中。
 *
 * 2.这样会挂起运行中的代码，并保持协程取消状态直到任务完成。
 */


fun main() {
    println("*********** 取消协程后使用NoCancellable CoroutineContext释放资源 ****************")
    noCancellableCoroutineContext()


}

fun noCancellableCoroutineContext() {
    runBlocking {
        val job = launch {
            try {
                repeat(1000) {
                    println(" i am sleeping $it")
                    delay(500)
                }
            } finally {
                //协程取消后必须使用挂起函数NonCancellable
                withContext(NonCancellable) {
                    println("job I'm running finally ...")
                    delay(1000)
                    println("job And I've just delay for 1 second because I'm non-cancellable")
                    cancelCoroutineScope()
                }
            }
        }
        delay(800)
        println("main: I'm tried waiting!")
        job.cancelAndJoin()
        println("main: Now I cam quit !")
    }
}
















