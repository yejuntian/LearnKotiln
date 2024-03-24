package studykotlin.dongnao

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.yield

/**
 * 协程的异常处理
 *
 * 一、根协程的异常传播
 *@see testRootCoroutineException()
 *
 * 1.自动传播异常：
 * launch与actor，当构建器创建一个‘根协程’时（该协程不是另一个协程的子协程），
 * 异常发行时第一时间抛出
 *
 * 2.向用户暴露异常：
 * async与produce，当构建器创建一个‘根协程’时（该协程不是另一个协程的子协程），
 * 异常发行时依赖用户最终消费异常，例如await和receive。
 *
 * 二、非根协程的异常
 * 其他协程创建的协程中，产生的异常总是会被传播。
 *@see testNotRootCoroutineException()
 *
 *
 * 异常传播特性：
 * 当一个协程发生异常运行失败时，它会传播这个异常给它的父级。
 * 接下来父级会进行下面几个操作：
 * ①取消它所有的子协程
 * ②取消它自己
 * ③将异常传播给它的父级
 *
 * supervisorJob：打破异常传播特性->作用等同于supervisorScope
 *@see testSupervisorJob()
 *
 * 1.使用supervisorJob时，一个子协程的失败不会影响其他子协程。
 * 2.SupervisorJob 不会传递给它的父级，它会'让子协程自己处理异常'。
 * 3.当在作用域内发生异常，其他子协程会全部取消。
 *
 * 这种需求常见于在作用域定义作业组件，如果任何一个UI子作业执行失败，
 * 它并不会取消整个UI组件，但是如果整个UI组件被销毁了，它的结果不再需要了，
 * 它就有必要使所有的子作业执行失败(supervisorJob对象.cancel()方法)。
 *
 * supervisorScope:打破异常传播特性
 *  当在作用域内发生异常，其他子协程会全部取消。
 * @see supervisorScope2()
 */


fun main() {
    println("*********** 根协程的异常传播 ****************")
    testRootCoroutineException()

    println("*********** 非根协程的异常传播 ****************")
    testNotRootCoroutineException()

    println("*********** supervisorJob：打破异常传播特性 ****************")
    testSupervisorJob()

    println("*********** supervisorScope：当在作用域内失败，其他子协程被取消 ****************")
    supervisorScope2()


}

fun testRootCoroutineException() {
    runBlocking {
        val job = GlobalScope.launch {
            try {
                throw IndexOutOfBoundsException("角标越界异常")
            } catch (e: Exception) {
                println("catch IndexOutOfBoundsException")
            }

        }
        job.join()

        val deferred = GlobalScope.async {
            throw ArithmeticException("参数异常")
        }

        try {
            deferred.await()
        } catch (e: Exception) {
            println("catch ArithmeticException")
        }

    }
}

fun testNotRootCoroutineException() {
    runBlocking {
        val job = GlobalScope.launch {
            async {
                // 如果async抛出异常，launch就会立即抛出异常，而不会调用.await()
                throw ArithmeticException("参数异常")
            }
        }
        job.join()
    }
}

fun testSupervisorJob() {
    runBlocking {
        val scope = CoroutineScope(SupervisorJob())
        val job1 = scope.launch {
            delay(100)
            println("child1")
            throw IndexOutOfBoundsException()
        }

        val job2 = scope.launch {
            try {
                delay(3000)
            } catch (e: Exception) {
                println("catch child2 exception")
            }
            println("child2 finished")
        }

        val job3 = scope.launch {
            delay(200)
            println("child3")
        }
        //所有协程等待
        joinAll(job1, job2, job3)
    }
}

fun supervisorScope2() {
    runBlocking {
        supervisorScope {
            val child = launch {
                try {
                    println("the child is sleeping ")
                    delay(3000)
                } finally {
                    println("the child is canceled ")
                }
            }

            val child2 = launch {
                println("the child2 is sleeping ")
                try {
                    delay(200)
                } finally {
                    println("the child2 is canceled ")
                }
            }

            //出让执行权
            yield()
            println("throwing a exception from the coroutineScope")
            throw IllegalAccessError()
        }
    }
}







